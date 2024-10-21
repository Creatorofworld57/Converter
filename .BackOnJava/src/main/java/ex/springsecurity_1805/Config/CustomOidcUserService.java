package ex.springsecurity_1805.Config;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
    public class CustomOidcUserService extends OidcUserService {

        @Override
        public OidcUser loadUser(OidcUserRequest userRequest) {
            // Вызываем стандартную логику Spring Security для загрузки пользователя
            OidcUser oidcUser = super.loadUser(userRequest);

            // Ваша кастомная логика для определения ролей пользователя
            Set<GrantedAuthority> mappedAuthorities = Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

            // Пример кастомной логики на основе email


            // Возвращаем кастомного пользователя с назначенными ролями
            return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        }
    }

