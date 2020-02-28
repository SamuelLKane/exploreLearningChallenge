package com.samuelkane.exploreLearningChallenge;

import com.samuelkane.exploreLearningChallenge.domain.User;
import com.samuelkane.exploreLearningChallenge.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@SpringBootApplication
public class ExploreLearningChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExploreLearningChallengeApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository) {
		return args -> {
			userRepository.save(new User((long) 1,"Samuel","Kane"));
			userRepository.save(new User((long) 2,"Robin","Macklin"));
			userRepository.save(new User((long) 3,"Carlos","Vizcaino"));
		};
	}
}
