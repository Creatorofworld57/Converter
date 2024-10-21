package ex.springsecurity_1805;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
@EnableAsync
@EnableCaching
public class SpringSecurity1805Application {
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurity1805Application.class, args);
	}

}
