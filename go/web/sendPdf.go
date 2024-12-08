package web

import (
	"fmt"
	"net/http"
	"os"
)

func sendPdfToServer(pdfPath, pdfFileName, username string) error {
	pdfFile, err := os.Open(pdfPath)
	if err != nil {
		return fmt.Errorf("could not open PDF file: %v", err)
	}
	defer pdfFile.Close()

	requestUrl := fmt.Sprintf("http://localhost:8080/api/pdf?filename=%s&username=%s", pdfFileName, username)
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
