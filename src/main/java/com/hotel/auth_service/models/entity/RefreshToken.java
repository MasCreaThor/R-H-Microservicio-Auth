// src/main/java/com/hotel/auth_service/models/entity/RefreshToken.java

package com.hotel.auth_service.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "refreshTokens")
public class RefreshToken {
    @Id
    private String id;
    
    private String token;
    
    @DocumentReference
    private User user;
    
    private Date expiryDate;
}