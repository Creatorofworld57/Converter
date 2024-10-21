package ex.springsecurity_1805.services;

<<<<<<< HEAD
import ex.springsecurity_1805.Models.*;
=======
import com.fasterxml.jackson.annotation.JsonView;
import com.github.javafaker.Faker;
import ex.springsecurity_1805.Models.*;
import ex.springsecurity_1805.Repositories.AudioRepository;
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625
import ex.springsecurity_1805.Repositories.ImageRepository;
import ex.springsecurity_1805.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
<<<<<<< HEAD
=======
import org.springframework.cache.annotation.Cacheable;
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
<<<<<<< HEAD
=======
import java.util.stream.Collectors;
import java.util.stream.IntStream;
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625


@Service
@AllArgsConstructor
public class ServiceApp {
    private List<Application> applicationList;
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private ImageRepository imageRepository;



<<<<<<< HEAD
=======
    public Application applicationById(int id) {

        return applicationList.stream()
                .filter(app -> app.getId() == id)
                .findFirst().orElse(null);
    }


    public Application applicationByName(String name) {
        return applicationList.stream()
                .filter(app -> Objects.equals(app.getName(), name))
                .findFirst().orElse(null);
    }
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625

    public void addUser(HttpServletRequest request, MultipartFile file) throws IOException {
        Img img = new Img();
        Usermain user = new Usermain();
        user.setName(request.getParameter("name"));
        user.setPassword(request.getParameter("password"));
        user.setRoles(request.getParameter("roles"));

        if (file != null && file.getSize() != 0) {
            img = ServiceHelperFunctions.toImgEntity(file);
            img.setPreview(true);
            user.addImgToProduct(img);
        }
        Date date = new Date();
        user.setUpdated(date);
        user.setCreated(date);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Usermain userok = repository.save(user);
        Img img1 = imageRepository.save(img);
        userok.setPreviewImageId(img1.getId());
        repository.save(userok);

    }

    @Transactional
    public void updateUser(String name, String password, String tele, String git, MultipartFile file, UserDEtailsService model) throws IOException {
        Optional<Usermain> optUser = repository.findByName(model.getUsername());
        if (optUser.isPresent()) {
            Usermain user = optUser.get();

            // Обработка изображения
            if (file != null && !file.isEmpty() && !"blob".equals(file.getOriginalFilename())) {
                Img img2 = ServiceHelperFunctions.toImgEntity(file);
                img2.setPreview(true);

                if (user.getPreviewImageId() != null) {
                    Optional<Img> optImg = imageRepository.findById(user.getPreviewImageId());
                    if (optImg.isPresent()) {
                        Img img = optImg.get();
                        img.setName(img2.getName());
                        img.setOriginalFileName(img2.getOriginalFileName());
                        img.setContentType(img2.getContentType());
                        img.setBytes(img2.getBytes());
                        img.setSize(img2.getSize());
                        imageRepository.save(img);

                    }
                } else {
                    user.addImgToProduct(img2);
                    Img  img3 = imageRepository.save(img2);
                    user.addImgToProduct(img3);
                    user.setPreviewImageId(img3.getId());

                    System.out.println("Изображение привязано");
                }
            }

            // Обновление пароля
            if (password != null && !password.trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }

            // Обновление имени
            if (name != null) {
                user.setName(name);
            }

            // Обновление социальных ссылок
            List<String> socialLinks = user.getSocial();
            if (socialLinks == null) {
                socialLinks = new LinkedList<>();
            }

            // Обновление git ссылки
            if (git != null) {
                if (!socialLinks.isEmpty() && socialLinks.get(0).startsWith("https://t.me")) {
                    socialLinks.addFirst(git);
                } else {
                    socialLinks.add(git);
                }
            }

            // Обновление tele ссылки
            if (tele != null) {
                if (!socialLinks.isEmpty() && socialLinks.get(0).startsWith("https://t.me")) {
                    socialLinks.add(1, tele);
                } else {
                    socialLinks.add(tele);
                }
            }

            user.setSocial(socialLinks);

            // Обновление даты
            user.setUpdated(new Date());

            // Сохранение пользователя
            repository.save(user);
        }
    }



    @Transactional
    public void deleteUser(UserDEtailsService model) {
        System.out.println("delete");
        try {
            Optional<Usermain> userOpt = repository.findByName(model.getUsername());

            if (userOpt.isPresent()) {
                Usermain user = userOpt.get();
                Long previewImageId = user.getPreviewImageId();

                if (previewImageId != null) {
                    Img image = imageRepository.findById(previewImageId)
                            .orElseThrow(() -> new RuntimeException("Image not found"));
                    image.setUser(null);
                    user.setPreviewImageId(null);
                    imageRepository.delete(image);
                }

                repository.delete(user);

            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при удалении пользователя: " + ex.getMessage(), ex);
        }
    }



<<<<<<< HEAD
=======
    public void audioKeep(MultipartFile file) throws IOException {
        Audio audio = new Audio();
        audio.setBuffer(file.getBytes());
        audio.setName(file.getOriginalFilename());
        audio.setContentType(file.getContentType());
        audio.setSize(file.getSize());
        audioRepository.save(audio);
    }
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625

    public void newUserWithOAuth(OAuth2User principal) throws IOException {
        Usermain usermain = new Usermain();
        Object SocialValue = principal.getAttributes().get("html_url");
        Object NameValue = principal.getAttributes().get("login");
        Object PasswordValue = principal.getAttributes().get("id");
        Object UpdatedValue = principal.getAttributes().get("created_at");
        Object CreatedValue = principal.getAttributes().get("updated_at");
        Object URL_ImgValue = principal.getAttributes().get("avatar_url");

      // MultipartFile file = downloadFileFromUrl(URL_ImgValue.toString());
      // Img img = toImgEntity(file);
     //  Img img1=imageRepository.save(img);




        usermain.setSocial(Collections.singletonList(SocialValue.toString()));
        usermain.setName(NameValue.toString());
        usermain.setPassword(passwordEncoder.encode(PasswordValue.toString()));
        Date date=new Date();
        usermain.setCreated(date);
        usermain.setUpdated(date);
        Usermain user1 = repository.save(usermain);
        //user1.setPreviewImageId(img1.getId());
        repository.save(user1);
    }

<<<<<<< HEAD

=======
  public List<Audio>searchTrackFromBD(String name) {
       List<Audio> list = audioRepository.findByNameContainingIgnoreCase(name);
       if(list.isEmpty()){
           String translate =ServiceHelperFunctions.KeyboardLayoutConverter.convertToEnglish(name);
          list = audioRepository.findByNameContainingIgnoreCase(translate);
          return  list.stream()
                   .sorted(Comparator.comparingInt(audio -> ServiceHelperFunctions.levenshteinDistance(audio.getName(),translate ))).collect(Collectors.toList());
       }

      return  list.stream()
              .sorted(Comparator.comparingInt(audio -> ServiceHelperFunctions.levenshteinDistance(audio.getName(), name))).collect(Collectors.toList());
  }

>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625

}
