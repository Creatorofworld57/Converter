package utilities

import (
	"fmt"
	"net/http"
	"os"
	"os/exec"
	"strconv"
)

func ConvertExtractPdf(mainPdfPath, outputPdfPath, strStart, strEnd string) error {
	pdftkPath := os.Getenv("PDFTK_PATH")
	if pdftkPath == "" {
		return fmt.Errorf("PDFTK path not set in environment variable")
	}

	start, err := strconv.Atoi(strStart)
	if err != nil {
		return fmt.Errorf("Invalid 'strstart' parameter", http.StatusBadRequest)
	}

	end, err := strconv.Atoi(strEnd)
	if err != nil {
		return fmt.Errorf("Invalid 'strend' parameter")
	}

	cmd := exec.Command("pdftk", mainPdfPath, "cat", fmt.Sprintf("%d-%d", start, end), "output", outputPdfPath)
	err = cmd.Run()
	if err != nil {
		return fmt.Errorf("Failed to extract pages: %v", err)
	}
	err = cmd.Run()
	if err != nil {
		return fmt.Errorf("error merging PDFs: %v", err)
	}

	return nil
}

func ConvertMergePdf(pdfFiles []string, outputPdfPath string) error {
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

func ConvertWatermarkPdf(mainPdfPath, watermarkPdfPath, outputPdfPath string) error {
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
