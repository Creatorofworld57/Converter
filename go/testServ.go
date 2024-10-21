package main

import (
	"fmt"
	"io"
	"net/http"
	"os"
)

func handlePdfUpload(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	file, err := os.Create("received.pdf")
	if err != nil {
		http.Error(w, "unable to create file", http.StatusInternalServerError)
		return
	}
	defer file.Close()

	_, err = io.Copy(file, r.Body)
	if err != nil {
		http.Error(w, "failed to save file", http.StatusInternalServerError)
		return
	}

	fmt.Fprintln(w, "PDF received successfully")
}

func main() {
	http.HandleFunc("/api/pdf", handlePdfUpload)
	fmt.Println("server listening on port 8080...")
	err := http.ListenAndServe(":8080", nil)
	if err != nil {
		fmt.Printf("error starting server: %v\n", err)
		os.Exit(1)
	}
}
