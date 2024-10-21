package ex.springsecurity_1805.services;

import ex.springsecurity_1805.Models.Audio;
import ex.springsecurity_1805.Repositories.AudioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class RedisService {

    @Autowired
    private AudioRepository audioRepository;

    @Transactional
    @Cacheable(value = "Audio", key = "#id")
    public Audio getAudioById(Long id){
        Optional<Audio> ref = audioRepository.findAudioById(id);
        return ref.orElse(null);
    }
}
