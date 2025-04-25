package com.hotel.auth_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.hotel.authservice.repositories")
@EnableMongoAuditing
public class MongoConfig {
    // La configuración básica está habilitada a través de las anotaciones
}