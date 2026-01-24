package com.app.TurfBookingApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //enable schedule for 'completion'
public class TurfBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurfBookingApplication.class, args);
	}
}
