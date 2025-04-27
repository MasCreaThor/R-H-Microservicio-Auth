//src/main/java/com/hotel/auth_service/config/MongoConfig.java

package com.hotel.auth_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.hotel.auth_service.repositories")
@EnableMongoAuditing
public class MongoConfig {
}