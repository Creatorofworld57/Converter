package ex.springsecurity_1805.Repositories;

import ex.springsecurity_1805.Models.Audio;

import jakarta.transaction.Transactional;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface AudioRepository extends JpaRepository<Audio, Long> {

    Optional<Audio> findAudioById(Long id);

    @Query("SELECT e.name FROM Audio e ORDER BY e.id ASC")
    List<String> findAllByOrderByIdAsc();

    List<Audio> findByNameContainingIgnoreCase(String title); // Поиск названий, которые содержат введенную подстроку

    Optional<Audio> findAudioByName(String name);

    @Query(value = "SELECT id FROM audio WHERE id > :startId ORDER BY id ASC LIMIT 5", nativeQuery = true)
    List<Long> findNextFiveTracks(@Param("startId") Long startId);

    @Query(value = "SELECT id FROM audio WHERE id < :startId ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Long> findPreviousFiveTracks(@Param("startId") Long startId);

    Optional<Audio> findTopByNameOrderByIdAsc(String name);

    @NonNull List<Audio> findAllById(@NonNull Iterable<Long> ids);



}
