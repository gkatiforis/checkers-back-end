package com.katiforis.top10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Top10Application  {
	public static void main(String[] args) {
		SpringApplication.run(Top10Application.class, args);
	}
}

