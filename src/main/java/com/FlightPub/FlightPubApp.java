package com.FlightPub;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@EnableMongoRepositories
@Controller
public class FlightPubApp {

	public static void main(String[] args) {
		SpringApplication.run(FlightPubApp.class, args);
	}
}

