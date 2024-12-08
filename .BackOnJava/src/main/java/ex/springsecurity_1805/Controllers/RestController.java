package ex.springsecurity_1805.Controllers;



import ex.springsecurity_1805.Models.*;

import ex.springsecurity_1805.Repositories.UserRepository;
import ex.springsecurity_1805.services.ServiceApp;
import ex.springsecurity_1805.services.UserDEtailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestController {
    private final  UserRepository rep;
    private final ServiceApp serviceApp;

    @Value("${urlFront}")
    String url;

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @GetMapping("/user/withGithub/")
    public String addUserWithGitHub(){
        Long id =rep.count();
       return id.toString();
    }



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
    

    @GetMapping("/wel")
    public String sdf(){
        return "foto";
    }

    @GetMapping("/get_user")
    public String getUser(@AuthenticationPrincipal UserDEtailsService userDEtailsService){
        String user = rep.findByName(userDEtailsService.getUsername()).get().getName();
        Name name = new Name();
        name.setName(user);
        return user;
        //String.format("{\"name\": \"%s\"}", user);
    }

}
