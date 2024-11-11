package ex.springsecurity_1805;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
@EnableAsync

public class ConverterStarter {
	public static void main(String[] args) {
		SpringApplication.run(ConverterStarter.class, args);
	}

}
