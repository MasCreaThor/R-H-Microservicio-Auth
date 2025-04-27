// src/main/java/com/hotel/auth_service/models/entity/Role.java

package com.hotel.auth_service.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "roles")
public class Role {
    @Id
    private String id;
    private String name;
    
    public Role(String name) {
        this.name = name;
    }
}