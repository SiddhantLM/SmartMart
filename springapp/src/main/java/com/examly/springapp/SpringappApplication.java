package com.examly.springapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

// import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableAsync
public class SpringappApplication {

	public static void main(String[] args) {
		// Dotenv dotenv =
		// Dotenv.configure().directory("D:\\programs\\JavaProject\\springapp\\.env").load();
		// dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

		SpringApplication.run(SpringappApplication.class, args);
	}

}
