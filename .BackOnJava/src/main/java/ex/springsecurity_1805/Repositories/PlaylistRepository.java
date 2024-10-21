package ex.springsecurity_1805.Repositories;

import ex.springsecurity_1805.Models.Playlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist,Long> {

    Optional<Playlist> findPlaylistById(Long id);

    Optional<Playlist> findByName(String name);
}
