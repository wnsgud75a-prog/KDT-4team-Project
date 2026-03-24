package com.project.join;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoinApplication.class, args);
	}

}
