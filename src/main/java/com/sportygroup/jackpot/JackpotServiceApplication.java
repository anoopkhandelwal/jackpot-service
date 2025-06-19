package com.sportygroup.jackpot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main entry point for the Spring Boot application.
 * This class uses @SpringBootApplication which is a convenience annotation that adds:
 * - @Configuration: Tags the class as a source of bean definitions for the application context.
 * - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings,
 * other beans, and various property settings.
 * - @ComponentScan: Tells Spring to look for other components, configurations, and services
 * in the `com.sportygroup.jackpot` package, allowing it to find controllers, services, etc.
 *
 * This version uses Spring WebFlux for reactive programming.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.sportygroup.jackpot")
public class JackpotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JackpotServiceApplication.class, args);
        System.out.println("Bet Application Service Started!!!");
        System.out.println("Access Bet API at: http://localhost:8080/api/bets");
        System.out.println("Access Reward API at: http://localhost:8080/api/jackpots/evaluate-reward");
    }
}