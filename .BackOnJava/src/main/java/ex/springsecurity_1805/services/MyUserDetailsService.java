package ex.springsecurity_1805.services;

import ex.springsecurity_1805.Repositories.UserRepository;
import ex.springsecurity_1805.Models.Usermain;


import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@AllArgsConstructor
public class MyUserDetailsService  implements UserDetailsService {

    private UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("good auth");
        Optional<Usermain> user = repository.findByName(username);
        return user.map(UserDEtailsService::new)
                .orElseThrow(()->new UsernameNotFoundException(" ${username}not found"));
    }
}