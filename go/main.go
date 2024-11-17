package main

import (
	"fmt"
	"io"
	"net/http"
	"os"
	"os/exec"
	"path/filepath"
	"time"
)

func nameFileTime(baseName string) string {
	timestamp := time.Now().Format("20060102_150405")
	return fmt.Sprintf("%s_%s", baseName, timestamp)
}

func convertToPdf(FilePath, outputPdfPath, method string) error {
	libreOfficePath := os.Getenv("LIBREOFFICE_PATH")
	if libreOfficePath == "" {
		return fmt.Errorf("libreOffice path not set in environment variable")
	}
	switch method {
	case "docxtopdf":
		cmd := exec.Command(libreOfficePath, "--headless", "--convert-to", "pdf", "--outdir", outputPdfPath, FilePath)
		err := cmd.Run()
		if err != nil {
			return fmt.Errorf("error converting docx to pdf: %v", err)
		}
	case "jpgtopdf":
		cmd := exec.Command(libreOfficePath, "--headless", "--convert-to", "pdf", "--outdir", outputPdfPath, FilePath)
		err := cmd.Run()
		if err != nil {
			return fmt.Errorf("error converting docx to pdf: %v", err)
		}
	case "xlstopdf":
		cmd := exec.Command(libreOfficePath, "--headless", "--convert-to", "pdf", "--outdir", outputPdfPath, FilePath)
		err := cmd.Run()
		if err != nil {
			return fmt.Errorf("error converting docx to pdf: %v", err)
		}
	}
	return nil
}

func convertMergePdf(pdfFiles []string, outputPdfPath string) error {
	pdftkPath := os.Getenv("PDFTK_PATH")
	if pdftkPath == "" {
		return fmt.Errorf("PDFTK path not set in environment variable")
	}

	args := append(pdfFiles, "cat", "output", outputPdfPath)

	cmd := exec.Command(pdftkPath, args...)
	err := cmd.Run()
	if err != nil {
		return fmt.Errorf("error merging PDFs: %v", err)
	}

	return nil
}

func convertWatermarkPdf(mainPdfPath, watermarkPdfPath, outputPdfPath string) error {
	pdftkPath := os.Getenv("PDFTK_PATH")
	if pdftkPath == "" {
		return fmt.Errorf("pdftk path not set in environment variable")
	}

	cmd := exec.Command(pdftkPath, mainPdfPath, "background", watermarkPdfPath, "output", outputPdfPath)
	err := cmd.Run()
	if err != nil {
		return fmt.Errorf("error applying watermark: %v", err)
	}

	return nil
}

func sendPdfToServer(pdfPath, pdfFileName string) error {
	pdfFile, err := os.Open(pdfPath)
	if err != nil {
		return fmt.Errorf("could not open PDF file: %v", err)
	}
	defer pdfFile.Close()

	requestUrl := fmt.Sprintf("http://localhost:8080/api/pdf?filename=%s", pdfFileName)
	req, err := http.NewRequest("POST", requestUrl, pdfFile)
	if err != nil {
		return fmt.Errorf("failed to create request: %v", err)
	}
	req.Header.Set("Content-Type", "application/pdf")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return fmt.Errorf("failed to send request: %v", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("server responded with status: %s", resp.Status)
	}
	return nil
}

func handleUploadPdfWatermark(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

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

		outputPdfName = nameFileTime("watermarked") + ".pdf"
		outputPdfPath := filepath.Join(".", outputPdfName)

		err = convertWatermarkPdf(mainFilePath, watermarkFilePath, outputPdfPath)
		if err != nil {
			done <- fmt.Errorf("Failed to apply watermark: %v", err)
			return
		}

		err = sendPdfToServer(outputPdfPath, outputPdfName)
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

func handleUploadPdfMerge(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

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

		outputPdfName = nameFileTime("merged") + ".pdf"
		outputPdfPath := filepath.Join(".", outputPdfName)

		err = convertMergePdf(pdfFilePaths, outputPdfPath)
		if err != nil {
			done <- fmt.Errorf("Failed to merge PDF files: %v", err)
			return
		}

		err = sendPdfToServer(outputPdfPath, outputPdfName)
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

func handleUploadFileXlsToPdf(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	var pdfFileName string
	done := make(chan error)
	go func() {
		defer close(done)

		err := r.ParseMultipartForm(10 << 20)
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

		newBaseName := nameFileTime(handler.Filename[:len(handler.Filename)-len(filepath.Ext(handler.Filename))])
		xlsFilePath := filepath.Join(".", newBaseName+".docx")
		outputPdfPath := "."
		pdfFileName = newBaseName + ".pdf"
		pdfFilePath := filepath.Join(outputPdfPath, pdfFileName)

		dst, err := os.Create(xlsFilePath)
		if err != nil {
			done <- fmt.Errorf("Unable to save the file: %v", err)
			return
		}

		if _, err = io.Copy(dst, file); err != nil {
			done <- fmt.Errorf("Unable to save the file: %v", err)
			return
		}
		dst.Close()

		err = convertToPdf(xlsFilePath, outputPdfPath, "xlstopdf")
		if err != nil {
			done <- fmt.Errorf("Conversion failed: %v", err)
			return
		}

		err = sendPdfToServer(pdfFilePath, pdfFileName)
		if err != nil {
			done <- fmt.Errorf("Failed to send PDF to server: %v", err)
			return
		}
		if err = os.Remove(xlsFilePath); err != nil {
			done <- fmt.Errorf("Failed to delete JPEG file: %v", err)
			return
		}
		if err = os.Remove(pdfFilePath); err != nil {
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
	fmt.Fprintf(w, "File '%s' converted and sent successfully", pdfFileName)
}

func handleUploadFileJpgToPdf(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	var pdfFileName string
	done := make(chan error)

	go func() {
		defer close(done)

		err := r.ParseMultipartForm(10 << 20)
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

		newBaseName := nameFileTime(handler.Filename[:len(handler.Filename)-len(filepath.Ext(handler.Filename))])
		jpgFilePath := filepath.Join(".", newBaseName+".docx")
		outputPdfPath := "."
		pdfFileName = newBaseName + ".pdf"
		pdfFilePath := filepath.Join(outputPdfPath, pdfFileName)

		dst, err := os.Create(jpgFilePath)
		if err != nil {
			done <- fmt.Errorf("Unable to save the file: %v", err)
			return
		}

		if _, err = io.Copy(dst, file); err != nil {
			done <- fmt.Errorf("Unable to save the file: %v", err)
			return
		}
		dst.Close()

		err = convertToPdf(jpgFilePath, outputPdfPath, "jpgtopdf")
		if err != nil {
			done <- fmt.Errorf("Conversion failed: %v", err)
			return
		}

		err = sendPdfToServer(pdfFilePath, pdfFileName)
		if err != nil {
			done <- fmt.Errorf("Failed to send PDF to server: %v", err)
			return
		}

		if err = os.Remove(jpgFilePath); err != nil {
			done <- fmt.Errorf("Failed to delete JPEG file: %v", err)
			return
		}
		if err = os.Remove(pdfFilePath); err != nil {
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
	fmt.Fprintf(w, "File '%s' converted and sent successfully", pdfFileName)
}

func handleUploadFileDocxToPdf(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	var pdfFileName string
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

		newBaseName := nameFileTime(handler.Filename[:len(handler.Filename)-len(filepath.Ext(handler.Filename))])
		docxFilePath := filepath.Join(".", newBaseName+".docx")
		outputPdfPath := "."
		pdfFileName = newBaseName + ".pdf"
		pdfFilePath := filepath.Join(outputPdfPath, pdfFileName)

		dst, err := os.Create(docxFilePath)
		if err != nil {
			done <- fmt.Errorf("Unable to save the file: %v", err)
			return
		}

		if _, err = io.Copy(dst, file); err != nil {
			done <- fmt.Errorf("Unable to save the file: %v", err)
			return
		}
		dst.Close()

		err = convertToPdf(docxFilePath, outputPdfPath, "docxtopdf")
		if err != nil {
			done <- fmt.Errorf("Conversion failed: %v", err)
			return
		}

		err = sendPdfToServer(pdfFilePath, pdfFileName)
		if err != nil {
			done <- fmt.Errorf("Failed to send PDF to server: %v", err)
			return
		}

		if err = os.Remove(docxFilePath); err != nil {
			done <- fmt.Errorf("Failed to delete DOCX file: %v", err)
			return
		}

		if err = os.Remove(pdfFilePath); err != nil {
			done <- fmt.Errorf("Failed to delete PDF file: %v", err)
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
	fmt.Fprintf(w, "File '%s' converted and sent successfully", pdfFileName)
}

func corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "POST, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type")

		if r.Method == http.MethodOptions {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

func main() {
	http.HandleFunc("/upload/docxtopdf", handleUploadFileDocxToPdf)
	http.HandleFunc("/upload/jpgtopdf", handleUploadFileJpgToPdf)
	http.HandleFunc("/upload/xlstopdf", handleUploadFileXlsToPdf)
	http.HandleFunc("/upload/pdfmerge", handleUploadPdfMerge)
	http.HandleFunc("/upload/watermarkpdf", handleUploadPdfWatermark)

	fmt.Println("Server listening on port 8081...")
	err := http.ListenAndServe(":8081", corsMiddleware(http.DefaultServeMux))
	if err != nil {
		fmt.Printf("Error starting server: %v\n", err)
		os.Exit(1)
	}
}
