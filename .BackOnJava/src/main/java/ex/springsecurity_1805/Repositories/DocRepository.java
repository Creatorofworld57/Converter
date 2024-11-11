package ex.springsecurity_1805.Repositories;

import ex.springsecurity_1805.Models.DOC;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocRepository extends JpaRepository<DOC,Long> {
}
