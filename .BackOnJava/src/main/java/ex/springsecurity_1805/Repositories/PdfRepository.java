package ex.springsecurity_1805.Repositories;

import ex.springsecurity_1805.Models.PDF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PdfRepository extends JpaRepository<PDF,Long> {
    List<Long> findPDFByUserId(Long id);
}
