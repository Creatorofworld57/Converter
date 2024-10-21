package ex.springsecurity_1805.Controllers;

import ex.springsecurity_1805.Models.Usermain;
import ex.springsecurity_1805.Models.updateModel;
import ex.springsecurity_1805.Repositories.UserRepository;
import ex.springsecurity_1805.services.ServiceApp;
import ex.springsecurity_1805.services.UserDEtailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
<<<<<<< HEAD

=======
@CrossOrigin(origins="https://localhost:3000",allowCredentials = "true")
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RestControllerForUserCRUD {
    private ServiceApp serviceApp;
    private UserRepository rep;
    @Autowired
    private AuthenticationProvider provider; // Менеджер аутентификации

    @Autowired
    private UserDetailsService userDetailsService;
    @PostMapping("/user")
    public void addUser(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        System.out.println("hi");
        serviceApp.addUser(request, file);
        authenticateUserAndSetSession(request.getParameter("name"),request.getParameter("password"));
    }
    @PatchMapping("/user")
    public void updateUser(@RequestParam("file") MultipartFile file, updateModel modelUp, @AuthenticationPrincipal UserDEtailsService model, @AuthenticationPrincipal OAuth2User principal) throws IOException {

        if(model!=null){
            serviceApp.updateUser(modelUp.getName(), modelUp.getPassword(),modelUp.getTele(),modelUp.getGit(), file, model);
        System.out.println("update info about" + model.getUsername());}
        else {
            Object loginValue = principal.getAttributes().get("login");
            Usermain user = rep.findByName(loginValue.toString()).get();
            UserDEtailsService dEtailsService = new UserDEtailsService(user);
            serviceApp.updateUser(modelUp.getName(), modelUp.getPassword(),modelUp.getTele(),modelUp.getGit(), file, dEtailsService);
        }
    }
    @DeleteMapping("/user")
    public void deleteUser(@AuthenticationPrincipal UserDEtailsService model) {
        System.out.println(model.getUsername() + "has been deleted");
        serviceApp.deleteUser(model);
    }

    private void authenticateUserAndSetSession(String username, String password) {
        // 3. Загружаем пользователя по имени
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 4. Создаем объект для аутентификации
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        // 5. Аутентифицируем пользователя
        provider.authenticate(authenticationToken);

        // 6. Устанавливаем аутентификацию в SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    }
}
