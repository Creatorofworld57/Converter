package utilities

import (
	"fmt"
	"os"
	"os/exec"
)

func ConvertToPdf(FilePath, outputPdfPath, method string) error {
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
