package com.example.project.govtForm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class GovtFormApplication {

	public static void main(String[] args) {
		SpringApplication.run(GovtFormApplication.class, args);
	}

}
