package ex.springsecurity_1805.Repositories;

import ex.springsecurity_1805.Models.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository  extends JpaRepository<Img,Long> {

}
