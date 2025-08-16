/*
package com.linkrap.BE.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    @Bean
    S3Client s3Client(@Value("${APP_S3_REGION:us-east-1}") String region) {
        return S3Client.builder()
                .region(software.amazon.awssdk.regions.Region.of(region))
                .build();
    }
}*/
