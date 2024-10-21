package ex.springsecurity_1805.RedisRepositories;

import ex.springsecurity_1805.Models.Audio;
import org.springframework.data.repository.CrudRepository;

public interface AudioRedisRepository extends CrudRepository<Audio,Long> {

}
