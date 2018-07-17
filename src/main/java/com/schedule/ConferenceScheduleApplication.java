package com.schedule;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConferenceScheduleApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(ConferenceScheduleApplication.class, args);
	}

	@Override
	public void run(String... args) {
	}
}
