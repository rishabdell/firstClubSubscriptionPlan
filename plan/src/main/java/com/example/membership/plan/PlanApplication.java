package com.example.membership.plan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PlanApplication {

	/**
	 * Application entry point for the Membership Plan service.
	 * Runs a Spring Boot application exposing REST endpoints and an in-memory H2 database.
	 */
	public static void main(String[] args) {
		SpringApplication.run(PlanApplication.class, args);
	}

}
