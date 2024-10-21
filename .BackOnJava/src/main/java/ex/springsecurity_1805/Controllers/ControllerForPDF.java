package ex.springsecurity_1805.Controllers;

import ex.springsecurity_1805.Models.PDF;
import ex.springsecurity_1805.services.ServiceForPdf;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ControllerForPDF {

    private final ServiceForPdf serviceForPdf;

    @GetMapping("/pdf/{id}")
    public ResponseEntity<?> getPdf(@PathVariable Long id) {
        PDF pdf = serviceForPdf.getPdf(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(pdf.getContentType()))
                .contentLength(pdf.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(pdf.getBytes())));
    }

    @GetMapping("/pdfs")
    public List<Long> pdfs(@AuthenticationPrincipal UserDetails userDetails) {
        return serviceForPdf.getPdfs(userDetails);
    }

    @PostMapping("/pdf")
    public ResponseEntity<String> savePdf(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails principal) {
        try {
            // Вызываем сервис для сохранения PDF
            serviceForPdf.savePdf(file, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body("PDF успешно сохранен");
        } catch (IOException e) {
            // Логгируем и возвращаем ошибку
            // log.error("Ошибка при сохранении PDF", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при сохранении PDF");
        }
    }
}
