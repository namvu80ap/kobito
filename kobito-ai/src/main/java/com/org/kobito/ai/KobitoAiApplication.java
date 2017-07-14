package com.org.kobito.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class KobitoAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KobitoAiApplication.class, args);
	}

	@GetMapping(value = "/")
	public String index( ) {
		return "OK";
	}
}
