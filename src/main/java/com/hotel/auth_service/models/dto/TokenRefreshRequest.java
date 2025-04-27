// src/main/java/com/hotel/auth_service/models/dto/TokenRefreshRequest.java

package com.hotel.auth_service.models.dto;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}