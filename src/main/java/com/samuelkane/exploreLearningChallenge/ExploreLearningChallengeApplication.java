package com.samuelkane.exploreLearningChallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@SpringBootApplication
public class ExploreLearningChallengeApplication {

	public WebSecurityConfigurerAdapter webSecurityConfig(DataSource dataSource) {
		return new WebSecurityConfigurerAdapter() {
			@Override
			protected void configure(HttpSecurity http) throws Exception {
				http.authorizeRequests()
						.antMatchers("/").permitAll()
						.and().authorizeRequests().antMatchers("/h2-console/**").permitAll();

				http.csrf().disable();
				http.headers().frameOptions().sameOrigin();
			}

			@Override
			protected void configure(AuthenticationManagerBuilder auth) throws Exception {
				auth.jdbcAuthentication().dataSource(dataSource);
			}
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(ExploreLearningChallengeApplication.class, args);
	}

}
