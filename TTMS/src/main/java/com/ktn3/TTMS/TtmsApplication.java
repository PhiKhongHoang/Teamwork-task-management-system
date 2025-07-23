package com.ktn3.TTMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TtmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TtmsApplication.class, args);
	}

}
