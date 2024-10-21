package ex.springsecurity_1805.Controllers;



import com.fasterxml.jackson.annotation.JsonView;
import ex.springsecurity_1805.Models.*;
import ex.springsecurity_1805.Repositories.UserRepository;
import ex.springsecurity_1805.services.ServiceApp;
import ex.springsecurity_1805.services.UserDEtailsService;

<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
=======
import lombok.AllArgsConstructor;

>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


<<<<<<< HEAD

=======
@CrossOrigin(origins="https://localhost:3000",allowCredentials = "true")
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625
@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestController {
<<<<<<< HEAD
    private final  UserRepository rep;
    private final ServiceApp serviceApp;

    @Value("${urlFront}")
    String url;
=======
    private UserRepository rep;
    private ServiceApp serviceApp;
    private TrailerRepository trailerRepository;
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @GetMapping("/user/withGithub/")
    public String addUserWithGitHub(){
        Long id =rep.count();
       return id.toString();
    }

<<<<<<< HEAD

=======
    @JsonView(Views.Public.class)
    @GetMapping("/searchOfTrack/{name}")
    public List<Audio> searchOfTrack(@PathVariable String name){
      return serviceApp.searchTrackFromBD(name);
    }
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625





    //Deprecated
    @PreAuthorize("hasAuthority('SUPERVISIOR')")
    @GetMapping("/secret/{name}")
    public ResponseEntity<?> secret(@PathVariable String name) {
        System.out.println(name);
        Optional<Usermain> userOpt = rep.findByName(name);
        if (userOpt.isPresent()) {
            Usermain user = userOpt.get();
            String password = user.getPassword();
            System.out.println(passwordEncoder().encode(password));
            return ResponseEntity.ok(new BCryptPasswordEncoder().encode(passwordEncoder().encode(password)));
        } else
            return ResponseEntity.ok("No user with such name");

    }
    @Async
   // @CrossOrigin(origins="http://130.193.62.14/",allowCredentials = "true")
    @GetMapping("/authorization")
    public CompletableFuture<Mono<ResponseEntity<?>>> doYouHaveAuth(@AuthenticationPrincipal UserDEtailsService user, @AuthenticationPrincipal OAuth2User principal) throws IOException {

        if (principal==null && user==null ) {
            System.out.println("Не авторизован ");

            return CompletableFuture.completedFuture(Mono.just(ResponseEntity.status(201).build()));
        }
        else{
            if(principal!=null){
                System.out.println(" авторизован"+principal.getName());
                Object loginValue = principal.getAttributes().get("login");
                System.out.println(loginValue);
               if(rep.findByName(loginValue.toString()).isEmpty()){
                   serviceApp.newUserWithOAuth(principal);
               }
            }
            System.out.println(" авторизован");
            return CompletableFuture.completedFuture(Mono.just(ResponseEntity.status(200).build()));
        }
    }



<<<<<<< HEAD
=======
    }
    // Проверка на наличие имени в бд при регистрации
    @CrossOrigin(origins="https://localhost:3000",allowCredentials = "true")
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625
    @PostMapping("/checking")
    public ResponseEntity<?> checkUserName(@RequestBody Data data) {
        System.out.println(data.getName() + " существует");
        if (rep.findByName(data.getName()).isPresent()) {
            return ResponseEntity.status(201).build();
        } else {
            return ResponseEntity.status(200).build();
        }
    }

    @Async
    @GetMapping("/infoAboutUser")
    public CompletableFuture<Mono<Usermain>> infoAboutUser(@AuthenticationPrincipal UserDEtailsService user, @AuthenticationPrincipal OAuth2User principal) {
        if(user !=null) {
            Optional<Usermain> u = rep.findByName(user.getUsername());

            return CompletableFuture.completedFuture(Mono.just(u.orElse(null)));
        }
        else{
            Object loginValue = principal.getAttributes().get("login");
            Optional<Usermain> u = rep.findByName(loginValue.toString());

            assert u.orElse(null) != null;
            return CompletableFuture.completedFuture(Mono.just(u.orElse(null)));
        }
    }



    @Async
    @GetMapping("/socials")
    public CompletableFuture<Mono<Socials>> socials(@AuthenticationPrincipal UserDEtailsService userDEtailsService, @AuthenticationPrincipal OAuth2User principal){
        Socials social = new Socials();
        if(userDEtailsService!=null) {
            Optional<Usermain> us = rep.findByName(userDEtailsService.getUsername());
            if (us.isPresent() && !us.get().getSocial().isEmpty()) {

                social.setTelegram(us.get().getSocial().getFirst());
                social.setGit(us.get().getSocial().getLast());
            } else {
                social.setTelegram("tele");
                social.setGit("git");

            }
        }
        else{
            Object obj=principal.getAttributes().get("html_url");
            social.setTelegram("tele");
            social.setGit(obj.toString());
        }
        System.out.println(social.getGit());
        return CompletableFuture.completedFuture(Mono.just(social));
    }

    @PostMapping("/receivingSocials")
    public void receivingSocials(@RequestBody List<String>socials,@AuthenticationPrincipal UserDEtailsService user1){
        rep.findByName(user1.getUsername()).ifPresent(Usermain -> new Usermain().setSocial(socials));
    }
    
<<<<<<< HEAD


    @GetMapping("/wel")
    public String sdf(){
        return "foto";
    }
=======
    @GetMapping("/trailer")
    public ResponseEntity<?> getTrailer() {
        Trailer trailer = trailerRepository.findById(1L).orElse(null);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("video/mp4"))
                .body(new InputStreamResource(new ByteArrayInputStream(trailer.getSize())));
    }
    @PostMapping("/uploadTrailer")
    public void uploadTrailer(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("upload was successful");
        Trailer trailer = new Trailer();
        trailer.setSize(file.getBytes());
        trailerRepository.save(trailer);
    }

>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625


}
