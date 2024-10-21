package ex.springsecurity_1805.Config;


import ex.springsecurity_1805.Repositories.AudioRepository;
import ex.springsecurity_1805.Repositories.UserRepository;
import ex.springsecurity_1805.services.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
<<<<<<< HEAD
=======
import org.springframework.http.HttpHeaders;
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
<<<<<<< HEAD
@RequiredArgsConstructor
@EnableJpaRepositories( basePackages ="ex.springsecurity_1805.Repositories")
public class Configuration1{
   private final UserRepository repository;
   private final CustomOidcUserService customOidcUserService;

    @Value("${urlFront}")
    String url;
=======
@AllArgsConstructor
@EnableJpaRepositories( basePackages ="ex.springsecurity_1805.Repositories")
public class Configuration1{
    UserRepository repository;
    CustomOidcUserService customOidcUserService;
    AudioRepository audioRepository;
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625
    @Bean
    public UserDetailsService userDetailsService(){
       /* UserDetails admin0 = User.builder().username("admin0").password(encoder.encode("52")).roles("ADMIN").build();
        UserDetails admin1 = User.builder().username("admin1").password(encoder.encode("703.747")).roles("USER").build();
        UserDetails admin2 = User.builder().username("admin2").password(encoder.encode("18")).roles("USER").build();*/
     return new MyUserDetailsService(repository);
    }
    private final SameSiteCookieFilter sameSiteCookieFilter;


    @Bean
    public AudioRepository audioRepository () {
      return  audioRepository ;
    }
    @Bean
    static public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
<<<<<<< HEAD
                        .requestMatchers("api/login", "/api/audio/**", "/api/audioName/**", "api/authorization", "/api/checking",
                                "/api/uploadTrailer", "login/oauth2/authorization/github",
                                "/login/oauth2/git", "/login/oauth2/code/github", "/api/audioCount",
                                "/api/user/withGithub/{id}", "/api/searchOfTrack/{name}",
                                "/api/background/**", "/api/nextAudios","/api/wel").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user").permitAll()// Разрешить доступ без аутентификации
                        .requestMatchers("/newUser").anonymous() // Доступно только анонимным пользователям
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/ws/**").permitAll()
=======
                        .requestMatchers( "api/login", "/api/audio/**", "/api/audioName/**","api/authorization","/api/checking",
                                "/api/uploadTrailer","login/oauth2/authorization/github",
                                "/login/oauth2/git","/login/oauth2/code/github","/api/audioCount",
                                "/api/user/withGithub/{id}","/api/searchOfTrack/{name}",
                                "/api/background/**","/api/nextAudios").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user").permitAll()// Разрешить доступ без аутентификации
                        .requestMatchers("/newUser").anonymous() // Доступно только анонимным пользователям
                        .requestMatchers("/api/**").authenticated()
                                .requestMatchers("/ws/**").permitAll()
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625

                )

                .formLogin(formLogin -> formLogin
                        .loginPage(String.format("%s/login", url))
                        .loginProcessingUrl("/perform_login") // URL для обработки логина
<<<<<<< HEAD
                        .defaultSuccessUrl(String.format("%s/home",url),true)
=======
                        .defaultSuccessUrl("https://localhost:3000/")
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625
                        // URL после успешного логина
                        .failureUrl(String.format("%s/login",url))
                        // URL после неудачного логина
                        .passwordParameter("password") // Параметр пароля
                        .usernameParameter("name") // Параметр имени пользователя
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.oidcUserService(customOidcUserService)
                        )

                        .defaultSuccessUrl(String.format("%s/home",url))
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl(String.format("%s/login",url))
                        .permitAll()
                ) .addFilterBefore(sameSiteCookieFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

   /* @Bean
    public FilterRegistrationBean<SameSiteFilter> sameSiteFilter() {
        FilterRegistrationBean<SameSiteFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SameSiteFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }*/




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
