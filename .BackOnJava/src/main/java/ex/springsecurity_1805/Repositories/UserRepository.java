package ex.springsecurity_1805.Repositories;

import ex.springsecurity_1805.Models.Usermain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usermain,Long> {
Optional<Usermain> findByName(String username);


}
