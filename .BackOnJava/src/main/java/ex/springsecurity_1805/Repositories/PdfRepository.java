package ex.springsecurity_1805.Repositories;

import ex.springsecurity_1805.Models.PDF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PdfRepository extends JpaRepository<PDF,Long> {
    /**
     * Возвращает список ID PDF, связанных с указанным пользователем.
     *
     * @param userId ID пользователя.
     * @return Список ID PDF.
     */
    @Query("SELECT p.Id FROM PDF p WHERE p.user.id = :userId")
    List<Long> findPdfIdsByUserId(@Param("userId") Long userId);
    Optional<PDF> findPDFByOriginalFileName(String name);
    Optional<PDF> findPDFById (Long id);
    @Query("SELECT p.name FROM PDF p WHERE p.Id = :documentId")
    String getName(@Param("documentId") Long id);
}
