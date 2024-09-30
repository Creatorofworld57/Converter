package main

import (
	"fmt"
	"os"
	"os/exec"
)

func convertDocxToPdf(docxFilePath string, outputPdfPath string) error {
	cmd := exec.Command("C:\\Program Files\\LibreOffice\\program\\soffice.exe", "--headless", "--convert-to", "pdf", "--outdir", outputPdfPath, docxFilePath)

	// Выполнение команды
	err := cmd.Run()
	if err != nil {
		return fmt.Errorf("error converting do: %v", err)
	}

	return nil
}

func main() {
	docxFilePath := "C:\\Users\\Admin\\GolandProjects\\convPDF\\example.docx"
	outputPdfPath := "."

	err := convertDocxToPdf(docxFilePath, outputPdfPath)
	if err != nil {
		fmt.Printf("еrror: %v\n", err)
		os.Exit(1)
	}    cp -r /C:/Users/Admin/GolandProjects/convPDF/* ./golang/



		fmt.Println("готово")
}
