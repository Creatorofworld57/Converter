package main

import (
	"fmt"
	"io"
	"net/http"
	"os"
	"os/exec"
	"path/filepath"
)

func convertDocxToPdf(docxFilePath, outputPdfPath string) error {
	libreOfficePath := os.Getenv("LIBREOFFICE_PATH")
	if libreOfficePath == "" {
		return fmt.Errorf("libreOffice path not set in environment variable")
	}

	cmd := exec.Command(libreOfficePath, "--headless", "--convert-to", "pdf", "--outdir", outputPdfPath, docxFilePath)
	err := cmd.Run()
	if err != nil {
		return fmt.Errorf("error converting docx to pdf: %v", err)
	}
	return nil
}

func sendPdfToServer(pdfPath string) error {
	pdfFile, err := os.Open(pdfPath)
	if err != nil {
		return fmt.Errorf("could not open PDF file: %v", err)
	}
	defer pdfFile.Close()

	requestUrl := "http://localhost:8080/api/pdf"
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

func handleFileUpload(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

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
		defer dst.Close()

		if _, err = io.Copy(dst, file); err != nil {
			done <- fmt.Errorf("Unable to save the file: %v", err)
			return
		}

		err = convertDocxToPdf(docxFilePath, outputPdfPath)
		if err != nil {
			done <- fmt.Errorf("Conversion failed: %v", err)
			return
		}

		pdfFileName := handler.Filename[:len(handler.Filename)-len(filepath.Ext(handler.Filename))] + ".pdf"
		pdfFilePath := filepath.Join(outputPdfPath, pdfFileName)

		err = sendPdfToServer(pdfFilePath)
		if err != nil {
			done <- fmt.Errorf("Failed to send PDF to server: %v", err)
			return
		}

		done <- nil
	}()

	err := <-done
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	fmt.Fprintln(w, "File converted and sent successfully")
}

func main() {
	http.HandleFunc("/upload", handleFileUpload)
	fmt.Println("Server listening on port 8081...")
	err := http.ListenAndServe(":8081", nil)
	if err != nil {
		fmt.Printf("Error starting server: %v\n", err)
		os.Exit(1)
	}
}
