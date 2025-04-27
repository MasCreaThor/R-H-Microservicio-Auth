// src/main/java/com/hotel/auth_service/models/dto/MessageResponse.java

package com.hotel.auth_service.models.dto;

public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}