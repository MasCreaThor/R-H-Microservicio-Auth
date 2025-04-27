// src/main/java/com/hotel/auth_service/repositories/RoleRepository.java

package com.hotel.auth_service.repositories;

import com.hotel.auth_service.models.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
}
