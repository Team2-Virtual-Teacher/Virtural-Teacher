package com.alpha53.virtualteacher;

import com.alpha53.virtualteacher.services.contracts.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VirtualTeacherApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualTeacherApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
//			storageService.deleteAll();
			storageService.init();
		};
	}
}
