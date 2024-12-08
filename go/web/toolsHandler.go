package web

import (
	"fmt"
	"gittest/utilities"
	"io"
	"net/http"
	"os"
	"path/filepath"
)

func HandleUploadPdfExtraction(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	strStart := r.URL.Query().Get("strstart")
	strEnd := r.URL.Query().Get("strend")
	userName := r.URL.Query().Get("username")

	if strStart == "" || strEnd == "" || userName == "" {
		http.Error(w, "Missing required parameters: 'strstart' / 'strend' / 'username' ", http.StatusBadRequest)
		return
	}

	var outputPdfName string
	done := make(chan error)

	go func() {
		defer close(done)

		err := r.ParseMultipartForm(10 << 20) // 10MB
		if err != nil {
			done <- fmt.Errorf("File is too big")
			return
		}

		file, handler, err := r.FormFile("file")
		if err != nil {
			done <- fmt.Errorf("Error retrieving file: %v", err)
			return
		}
		defer file.Close()

		originalFileName := handler.Filename
		originalPdfPath := filepath.Join(".", originalFileName)

		dst, err := os.Create(originalPdfPath)
		if err != nil {
			done <- fmt.Errorf("Unable to save the file: %v", err)
			return
		}

		if _, err = io.Copy(dst, file); err != nil {
			done <- fmt.Errorf("Unable to save the file: %v", err)
			return
		}
		dst.Close()

		outputPdfName = utilities.NameFileTime("_extracted") + ".pdf"
		outputPdfPath := filepath.Join(".", outputPdfName)

		err = utilities.ConvertExtractPdf(originalPdfPath, outputPdfPath, strStart, strEnd)
		if err != nil {
			done <- fmt.Errorf("Failed extract pdf: %v", err)
			return
		}

		err = sendPdfToServer(outputPdfPath, outputPdfName, userName)
		if err != nil {
			done <- fmt.Errorf("Failed to send PDF to server: %v", err)
			return
		}

		if err = os.Remove(originalPdfPath); err != nil {
			done <- fmt.Errorf("Failed to delete original PDF file: %v", err)
			return
		}

		if err = os.Remove(outputPdfPath); err != nil {
			done <- fmt.Errorf("Failed to delete extracted PDF file: %v", err)
			return
		}

		done <- nil
	}()

	err := <-done
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		fmt.Println(err)
		return
	}

	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, "File '%s' created successfully", outputPdfName)
}

func HandleUploadPdfWatermark(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	userName := r.URL.Query().Get("username")

	var outputPdfName string
	done := make(chan error)

	go func() {
		defer close(done)

		err := r.ParseMultipartForm(50 << 20) // 50 MB
		if err != nil {
			done <- fmt.Errorf("Request size is too large")
			return
		}

		formFiles := r.MultipartForm.File["files"]
		if len(formFiles) < 2 {
			done <- fmt.Errorf("At least two files are required: main and watermark")
			return
		}

		mainFileHeader := formFiles[0]
		watermarkFileHeader := formFiles[1]

		mainFilePath := filepath.Join(".", mainFileHeader.Filename)
		mainFile, err := mainFileHeader.Open()
		if err != nil {
			done <- fmt.Errorf("Unable to open main file: %v", err)
			return
		}
		defer mainFile.Close()

		dstMain, err := os.Create(mainFilePath)
		if err != nil {
			done <- fmt.Errorf("Unable to save main file: %v", err)
			return
		}
		if _, err := io.Copy(dstMain, mainFile); err != nil {
			done <- fmt.Errorf("Error saving main file: %v", err)
			return
		}
		dstMain.Close()

		watermarkFilePath := filepath.Join(".", watermarkFileHeader.Filename)
		watermarkFile, err := watermarkFileHeader.Open()
		if err != nil {
			done <- fmt.Errorf("Unable to open watermark file: %v", err)
			return
		}
		defer watermarkFile.Close()

		dstWatermark, err := os.Create(watermarkFilePath)
		if err != nil {
			done <- fmt.Errorf("Unable to save watermark file: %v", err)
			return
		}
		if _, err := io.Copy(dstWatermark, watermarkFile); err != nil {
			done <- fmt.Errorf("Error saving watermark file: %v", err)
			return
		}
		dstWatermark.Close()

		outputPdfName = utilities.NameFileTime("watermarked") + ".pdf"
		outputPdfPath := filepath.Join(".", outputPdfName)

		err = utilities.ConvertWatermarkPdf(mainFilePath, watermarkFilePath, outputPdfPath)
		if err != nil {
			done <- fmt.Errorf("Failed to apply watermark: %v", err)
			return
		}

		err = sendPdfToServer(outputPdfPath, outputPdfName, userName)
		if err != nil {
			done <- fmt.Errorf("Failed to send PDF to server: %v", err)
			return
		}

		if err = os.Remove(mainFilePath); err != nil {
			done <- fmt.Errorf("Failed to delete PDF file: %v", err)
			return
		}
		if err = os.Remove(watermarkFilePath); err != nil {
			done <- fmt.Errorf("Failed to delete PDF file: %v", err)
			return
		}
		if err = os.Remove(outputPdfPath); err != nil {
			done <- fmt.Errorf("Failed to delete PDF file: %v", err)
			return
		}

		done <- nil
	}()

	err := <-done
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, "File '%s' created successfully", outputPdfName)
}

func HandleUploadPdfMerge(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	userName := r.URL.Query().Get("username")

	var outputPdfName string
	done := make(chan error)

	go func() {

		err := r.ParseMultipartForm(50 << 20) // 50MB
		if err != nil {
			done <- fmt.Errorf("Request size is too large")
			return
		}

		formFiles := r.MultipartForm.File["files"]
		if len(formFiles) == 0 {
			done <- fmt.Errorf("No files were uploaded")
			return
		}

		var pdfFilePaths []string

		for _, fileHeader := range formFiles {
			file, err := fileHeader.Open()
			if err != nil {
				done <- fmt.Errorf("Unable to open file: %v", err)
				return
			}

			defer file.Close()

			filePath := filepath.Join(".", fileHeader.Filename)
			dst, err := os.Create(filePath)
			if err != nil {
				done <- fmt.Errorf("Unable to save file: %v", err)
				return
			}

			if _, err := io.Copy(dst, file); err != nil {
				done <- fmt.Errorf("Unable to save file: %v", err)
				return
			}

			dst.Close()

			pdfFilePaths = append(pdfFilePaths, filePath)
		}

		outputPdfName = utilities.NameFileTime("merged") + ".pdf"
		outputPdfPath := filepath.Join(".", outputPdfName)

		err = utilities.ConvertMergePdf(pdfFilePaths, outputPdfPath)
		if err != nil {
			done <- fmt.Errorf("Failed to merge PDF files: %v", err)
			return
		}

		err = sendPdfToServer(outputPdfPath, outputPdfName, userName)
		if err != nil {
			done <- fmt.Errorf("Failed to send PDF to server: %v", err)
			return
		}

		for _, path := range pdfFilePaths {
			os.Remove(path)
		}

		if err = os.Remove(outputPdfPath); err != nil {
			done <- fmt.Errorf("Failed to delete PDF file: %v", err)
			return
		}

		done <- nil
	}()

	err := <-done
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, "File '%s' converted and sent successfully", outputPdfName)
}
