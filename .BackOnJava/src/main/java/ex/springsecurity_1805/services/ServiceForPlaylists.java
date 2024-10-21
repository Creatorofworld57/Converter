package ex.springsecurity_1805.services;

import com.fasterxml.jackson.annotation.JsonView;
import ex.springsecurity_1805.Models.Audio;
import ex.springsecurity_1805.Models.Playlist;
import ex.springsecurity_1805.Models.Views;
import ex.springsecurity_1805.Repositories.AudioRepository;
import ex.springsecurity_1805.Repositories.PlaylistRepository;
import ex.springsecurity_1805.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceForPlaylists {
    private final AudioRepository audioRepository;
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    @JsonView(Views.Public.class)
    public List<Audio> likedAudios(UserDetails userDetails){
       Optional<Playlist> playlistOptional =  playlistRepository.findByName(userRepository.findByName(userDetails.getUsername()).get().getId().toString());
       if(playlistOptional.isPresent()) {
           Playlist playlist = playlistOptional.get();
            List<Long> ids = playlist.getTracks();

          return audioRepository.findAllById(ids);
       }
       else{
           return null;
       }
    }


}
