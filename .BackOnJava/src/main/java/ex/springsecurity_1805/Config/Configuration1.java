package ex.springsecurity_1805.Config;


import ex.springsecurity_1805.Repositories.UserRepository;
import ex.springsecurity_1805.services.MyUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import java.time.Duration;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableJpaRepositories( basePackages ="ex.springsecurity_1805.Repositories")
public class Configuration1{
    UserRepository repository;
    CustomOidcUserService customOidcUserService;

    @Bean
    public UserDetailsService userDetailsService(){
       /* UserDetails admin0 = User.builder().username("admin0").password(encoder.encode("52")).roles("ADMIN").build();
        UserDetails admin1 = User.builder().username("admin1").password(encoder.encode("703.747")).roles("USER").build();
        UserDetails admin2 = User.builder().username("admin2").password(encoder.encode("18")).roles("USER").build();*/
     return new MyUserDetailsService(repository);
    }
    @Bean
    static public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "api/login", "/api/audio/**", "/api/audioName/**","api/authorization","/api/checking","/api/uploadTrailer","login/oauth2/authorization/github","/login/oauth2/git","/login/oauth2/code/github","/api/audioCount","/api/user/withGithub/{id}","/api/searchOfTrack/{name}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user").permitAll()// Разрешить доступ без аутентификации
                        .requestMatchers("/newUser").anonymous() // Доступно только анонимным пользователям
                        .requestMatchers("/api/**").authenticated()
                                .requestMatchers("/ws/**").permitAll()

                )

                .formLogin(formLogin -> formLogin
                        .loginPage("https://localhost:3000/login") // Путь к странице логина
                        .loginProcessingUrl("/perform_login") // URL для обработки логина
                        .defaultSuccessUrl("https://localhost:3000/profile")
                        // URL после успешного логина
                        .failureUrl("https://localhost:3000/login")
                        // URL после неудачного логина
                        .passwordParameter("password") // Параметр пароля
                        .usernameParameter("name") // Параметр имени пользователя
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.oidcUserService(customOidcUserService)
                        )

                       .defaultSuccessUrl("https://localhost:3000/")
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout") // URL для выхода
                        .logoutSuccessUrl("https://localhost:3000/login") // URL после успешного выхода
                        .permitAll()
                );

        return http.build();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofStrict().whenHasName("JSESSIONID");
    }
    @Bean
    CorsFilter corsFilter() {
        // Источник конфигураций CORS
        var corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        // Конфигурация CORS
        var globalCorsConfiguration = new CorsConfiguration();

        // Разрешаются CORS-запросы:

        globalCorsConfiguration.addAllowedOrigin("https://localhost:3000");

        globalCorsConfiguration.addAllowedHeader(HttpHeaders.AUTHORIZATION);
        globalCorsConfiguration.addAllowedHeader("X-CUSTOM-HEADER");
        globalCorsConfiguration.addAllowedHeader("Access-Control-Allow-Origin");
        globalCorsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        // - с передачей учётных данных
        globalCorsConfiguration.setAllowCredentials(true);
        // - с методами GET, POST, PUT, PATCH и DELETE
        globalCorsConfiguration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));

        globalCorsConfiguration.setExposedHeaders(List.of("X-OTHER-CUSTOM-HEADER"));

        globalCorsConfiguration.setMaxAge(Duration.ofSeconds(10));


        corsConfigurationSource.registerCorsConfiguration("/**", globalCorsConfiguration);

        return new CorsFilter(corsConfigurationSource);
    }



//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http//localhost:3000"));
//        config.setAllowedHeaders(List.of("Access-Control-Allow-Origin"));
//        config.setAllowedMethods(List.of("*"));
//        config.setAllowCredentials(true);
//
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }


}
