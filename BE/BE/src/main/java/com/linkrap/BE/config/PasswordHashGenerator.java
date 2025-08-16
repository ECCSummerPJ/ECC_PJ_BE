// src/main/java/com/linkrap/be/config/PasswordHashGenerator.java
package com.linkrap.BE.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordHashGenerator {

    @Bean
    public CommandLineRunner generateHash(PasswordEncoder passwordEncoder) {
        return args -> {
            String rawPassword = "linkrap918273645";
            String hash = passwordEncoder.encode(rawPassword);
            System.out.println("Generated BCrypt hash: " + hash);
        };
    }
}