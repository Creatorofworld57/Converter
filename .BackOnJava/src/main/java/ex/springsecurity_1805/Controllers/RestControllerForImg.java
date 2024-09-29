package ex.springsecurity_1805.Controllers;


import ex.springsecurity_1805.Models.Img;


import ex.springsecurity_1805.Models.Usermain;
import ex.springsecurity_1805.Repositories.ImageRepository;
import ex.springsecurity_1805.Repositories.UserRepository;


import ex.springsecurity_1805.services.ServiceHelperFunctions;
import ex.springsecurity_1805.services.UserDEtailsService;

import lombok.RequiredArgsConstructor;


import org.springframework.core.io.InputStreamResource;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
@CrossOrigin(origins="https://localhost:3000/profile",allowCredentials = "true")
@RestController
@RequiredArgsConstructor
public class RestControllerForImg {
    private final ImageRepository repository;
    private final UserRepository repo;


    @GetMapping("api/images/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        Img img = repository.findById(id).orElse(null);

        assert img != null;
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(img.getContentType()))
                .contentLength(img.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(img.getBytes())));
    }
    //отправка фото для профиля
    @GetMapping("api/userInfo")
    public String userInfo(@AuthenticationPrincipal UserDEtailsService s, @AuthenticationPrincipal OAuth2User user) {
        Long id = null;
        if (s == null && user ==null) {
            return "16";
        }

        if(s !=null) {
            Optional<Usermain> userOpt = repo.findByName(s.getUsername());


            if (userOpt.isPresent()) {
                Usermain us = userOpt.get();
                id = us.getPreviewImageId();
            }
        }

        else if( user !=null){
            Optional<Usermain> userOpt = repo.findByName(user.getAttributes().get("login").toString());
            if (userOpt.isPresent()) {
                Usermain us = userOpt.get();
                id = us.getPreviewImageId();
            }
        }

        if (id == null) {
            return "16";
        } else {
            return id.toString();
        }
    }
    @PostMapping("api/image")
    public void uploadImage(@AuthenticationPrincipal UserDEtailsService principal, @RequestParam("file")MultipartFile file) throws IOException {
            Usermain user = repo.findByName(principal.getUsername()).get();
            Img img = ServiceHelperFunctions.toImgEntity(file);
            img.setUser(user);
            repository.save(img);
    }
}
