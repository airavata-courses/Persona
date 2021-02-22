package com.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
	
	
	public static void main(String[] args) {
		 System.setProperty("spring.config.name", "gateway");
		SpringApplication.run(EurekaApplication.class, args);
	}

}
