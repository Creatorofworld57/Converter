package ex.springsecurity_1805.Controllers;



import com.fasterxml.jackson.annotation.JsonView;
import ex.springsecurity_1805.Models.*;
import ex.springsecurity_1805.Repositories.UserRepository;
import ex.springsecurity_1805.services.ServiceApp;
import ex.springsecurity_1805.services.UserDEtailsService;

import lombok.AllArgsConstructor;

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


@CrossOrigin(origins="https://localhost:3000",allowCredentials = "true")
@org.springframework.web.bind.annotation.RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RestController {
    private UserRepository rep;
    private ServiceApp serviceApp;


    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @GetMapping("/user/withGithub/")
    public String addUserWithGitHub(){
        Long id =rep.count();
       return id.toString();
    }







    //Deprecated

    @Async
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

    
    // Проверка на наличие имени в бд при регистрации
    @CrossOrigin(origins="https://localhost:3000",allowCredentials = "true")
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




    @PostMapping("/receivingSocials")
    public void receivingSocials(@RequestBody List<String>socials,@AuthenticationPrincipal UserDEtailsService user1){
        rep.findByName(user1.getUsername()).ifPresent(Usermain -> new Usermain().setSocial(socials));
    }
    



}
