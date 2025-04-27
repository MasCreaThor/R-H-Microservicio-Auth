// src/main/java/com/hotel/auth_service/models/dto/RegisterRequest.java

package com.hotel.auth_service.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    @NotBlank
    private String passwordConfirm;
    
    private String roleId;
    
    private PeopleRequest peopleInfo;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public PeopleRequest getPeopleInfo() {
        return peopleInfo;
    }

    public void setPeopleInfo(PeopleRequest peopleInfo) {
        this.peopleInfo = peopleInfo;
    }
}