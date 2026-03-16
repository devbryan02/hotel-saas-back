package com.app.hotelsaas.catin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CatInApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatInApplication.class, args);
    }

}
