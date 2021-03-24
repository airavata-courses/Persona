package com.google.drive.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
//@EnableDiscoveryClient
public class GoogleDriveAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoogleDriveAppApplication.class, args);
    }

}
