package com.example.SMGCodingAssesment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SmgCodingAssesmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmgCodingAssesmentApplication.class, args);
	}

}
