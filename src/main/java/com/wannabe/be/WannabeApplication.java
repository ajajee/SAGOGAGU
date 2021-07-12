package com.wannabe.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WannabeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WannabeApplication.class, args);
	}


	
}
