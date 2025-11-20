package com.efub.doppelganger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DoppelgangerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoppelgangerApplication.class, args);
	}

}
