package com.api.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//TODO Add Security
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Pms {

	public static void main(String[] args) {
		SpringApplication.run(Pms.class, args);
	}

}
