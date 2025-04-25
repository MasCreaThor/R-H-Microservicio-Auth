package com.hotel.auth_service.repositories;

import com.hotel.auth_service.models.entity.People;
import com.hotel.auth_service.models.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends MongoRepository<People, String> {
    Optional<People> findByUser(User user);
}
