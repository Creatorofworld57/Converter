package web

import (
	"fmt"
	"gittest/utilities"
	"io"
	"net/http"
	"os"
	"path/filepath"
)

func HandleUploadFileXlsToPdf(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	userName := r.URL.Query().Get("username")

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

		newBaseName := utilities.NameFileTime(handler.Filename[:len(handler.Filename)-len(filepath.Ext(handler.Filename))])
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

		err = utilities.ConvertToPdf(xlsFilePath, outputPdfPath, "xlstopdf")
		if err != nil {
			done <- fmt.Errorf("Conversion failed: %v", err)
			return
		}

		err = sendPdfToServer(pdfFilePath, pdfFileName, userName)
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

func HandleUploadFileJpgToPdf(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	userName := r.URL.Query().Get("username")

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

		newBaseName := utilities.NameFileTime(handler.Filename[:len(handler.Filename)-len(filepath.Ext(handler.Filename))])
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

		err = utilities.ConvertToPdf(jpgFilePath, outputPdfPath, "jpgtopdf")
		if err != nil {
			done <- fmt.Errorf("Conversion failed: %v", err)
			return
		}

		err = sendPdfToServer(pdfFilePath, pdfFileName, userName)
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

func HandleUploadFileDocxToPdf(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	userName := r.URL.Query().Get("username")

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

		newBaseName := utilities.NameFileTime(handler.Filename[:len(handler.Filename)-len(filepath.Ext(handler.Filename))])
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

		err = utilities.ConvertToPdf(docxFilePath, outputPdfPath, "docxtopdf")
		if err != nil {
			done <- fmt.Errorf("Conversion failed: %v", err)
			return
		}

		err = sendPdfToServer(pdfFilePath, pdfFileName, userName)
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
