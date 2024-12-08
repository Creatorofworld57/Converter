package main

import (
	"fmt"
	"gittest/web"
	"net/http"
	"os"
)

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

	http.HandleFunc("/upload/docxtopdf", web.HandleUploadFileDocxToPdf)
	http.HandleFunc("/upload/jpgtopdf", web.HandleUploadFileJpgToPdf)
	http.HandleFunc("/upload/xlstopdf", web.HandleUploadFileXlsToPdf)
	http.HandleFunc("/upload/pdfmerge", web.HandleUploadPdfMerge)
	http.HandleFunc("/upload/watermarkpdf", web.HandleUploadPdfWatermark)
	http.HandleFunc("/upload/pdfextraction", web.HandleUploadPdfExtraction)

	fmt.Println("Server listening on port 8081...")
	err := http.ListenAndServe(":8081", corsMiddleware(http.DefaultServeMux))
	if err != nil {
		fmt.Printf("Error starting server: %v\n", err)
		os.Exit(1)
	}
}
