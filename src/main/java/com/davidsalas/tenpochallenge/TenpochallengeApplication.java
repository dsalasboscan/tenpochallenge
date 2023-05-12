package com.davidsalas.tenpochallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.ZoneId;

@SpringBootApplication
@EnableCaching
public class TenpochallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TenpochallengeApplication.class, args);
	}


	@Bean
	public Clock clock() {
		return Clock.system(ZoneId.of("UTC"));
	}
}
