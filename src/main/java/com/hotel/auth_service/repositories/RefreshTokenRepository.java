// src/main/java/com/hotel/auth_service/repositories/RefreshTokenRepository.java

package com.hotel.auth_service.repositories;

import com.hotel.auth_service.models.entity.RefreshToken;
import com.hotel.auth_service.models.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}