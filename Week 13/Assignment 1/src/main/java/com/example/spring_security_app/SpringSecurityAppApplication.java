package com.example.spring_security_app;

import com.example.spring_security_app.config.RsaKeyConfigProperties;
import com.example.spring_security_app.entity.User;
import com.example.spring_security_app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableConfigurationProperties(RsaKeyConfigProperties.class)
@SpringBootApplication
public class SpringSecurityAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeUser(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			User user = new User();
			user.setUsername("sheren");
			user.setEmail("sheren@gmail.com");
			user.setPassword(passwordEncoder.encode("123"));

			userRepository.save(user);
		};
	}

}
