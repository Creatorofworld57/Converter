package main

import (
	"fmt"
	"io"
	"net/http"
	"os"
	"os/exec"
	"path/filepath"
)

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

func handleUploadFileXlsToPdf(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}
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

		xlsFilePath := filepath.Join(".", handler.Filename)
		outputPdfPath := "."

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

		pdfFileName := handler.Filename[:len(handler.Filename)-len(filepath.Ext(handler.Filename))]
		pdfFilePath := filepath.Join(outputPdfPath, pdfFileName)
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
	w.WriteHeader(http.StatusOK) // Отправляем статус 200 OK
	fmt.Fprintln(w, "File converted and sent successfully")
}

func handleUploadFileJpgToPdf(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}
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

		jpgFilePath := filepath.Join(".", handler.Filename)
		outputPdfPath := "."

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

		pdfFileName := handler.Filename[:len(handler.Filename)-len(filepath.Ext(handler.Filename))] + ".pdf"
		pdfFilePath := filepath.Join(outputPdfPath, pdfFileName)

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
	w.WriteHeader(http.StatusOK) // Отправляем статус 200 OK
	fmt.Fprintln(w, "File converted and sent successfully")
}

func handleUploadFileDocxToPdf(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	var pdfFileName string // Declare pdfFileName outside the goroutine
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

		docxFilePath := filepath.Join(".", handler.Filename)
		outputPdfPath := "."

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

		// Set pdfFileName here, accessible outside the goroutine
		pdfFileName = handler.Filename[:len(handler.Filename)-len(filepath.Ext(handler.Filename))] + ".pdf"
		pdfFilePath := filepath.Join(outputPdfPath, pdfFileName)

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

	// Wait for the goroutine to finish
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

	fmt.Println("Server listening on port 8081...")
	err := http.ListenAndServe(":8081", corsMiddleware(http.DefaultServeMux))
	if err != nil {
		fmt.Printf("Error starting server: %v\n", err)
		os.Exit(1)
	}
}
