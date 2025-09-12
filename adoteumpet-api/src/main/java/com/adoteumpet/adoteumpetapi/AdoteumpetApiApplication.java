package com.adoteumpet.adoteumpetapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AdoteumpetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdoteumpetApiApplication.class, args);
		System.out.println("API do AdoteUmPet está rodando!");
	}

}
