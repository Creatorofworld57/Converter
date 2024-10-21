package ex.springsecurity_1805.Repositories;

import ex.springsecurity_1805.Models.BackgroundImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BackImageRepository extends JpaRepository<BackgroundImage,Long> {
    Optional<BackgroundImage> findBackgroundImageById(Long id);
}
