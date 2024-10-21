package ex.springsecurity_1805.services;

import ex.springsecurity_1805.Models.PDF;
import ex.springsecurity_1805.Repositories.PdfRepository;
import ex.springsecurity_1805.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceForPdf {
    private final PdfRepository pdfRepository;
    private final UserRepository userRepository;
    public List<Long> getPdfs(UserDetails userDetails){
        Long id = userRepository.findByName(userDetails.getUsername()).get().getId();
        return pdfRepository.findPDFByUserId(id);
    }

    public PDF getPdf(Long id){
        Optional<PDF> opt = pdfRepository.findById(id);
        return opt.orElse(null);
    }
    public void savePdf(MultipartFile file,UserDetails userDetails) throws IOException {
        PDF pdf =new PDF();
        pdf.setName(file.getName());
        pdf.setSize(file.getSize());
        pdf.setContentType(file.getContentType());
        pdf.setBytes(file.getBytes());
        pdf.setOriginalFileName(file.getOriginalFilename());
        pdf.setUser(userRepository.findByName(userDetails.getUsername()).get());
        pdfRepository.save(pdf);
    }
}