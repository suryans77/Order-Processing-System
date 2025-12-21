package com.example.orderservicesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OrderServiceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceSystemApplication.class, args);
    }

}
