package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")  //Scan all the packages
@EnableMongoRepositories("com.example.repository") //Enable the repository
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Hello");
        System.out.println("Amo OP");
    }

}
