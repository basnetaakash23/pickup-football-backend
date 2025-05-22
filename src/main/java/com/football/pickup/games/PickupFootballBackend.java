package com.football.pickup.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class PickupFootballBackend {

	public static void main(String[] args) {
		SpringApplication.run(PickupFootballBackend.class, args);
	}

}
