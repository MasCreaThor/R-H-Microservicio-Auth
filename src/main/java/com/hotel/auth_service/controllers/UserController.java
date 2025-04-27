// src/main/java/com/hotel/auth_service/controllers/UserController.java

package com.hotel.auth_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.auth_service.models.entity.User;
import com.hotel.auth_service.repositories.UserRepository;
import com.hotel.auth_service.security.jwt.UserDetailsImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Obtiene la información del usuario autenticado
     * @return ResponseEntity con datos del usuario
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        Optional<User> user = userRepository.findById(userDetails.getId());
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.get().getId());
        response.put("email", user.get().getEmail());
        response.put("role", user.get().getRole() != null ? 
                Map.of("id", user.get().getRole().getId(), "name", user.get().getRole().getName()) : 
                null);
        response.put("createdAt", user.get().getCreatedAt());
        response.put("updatedAt", user.get().getUpdatedAt());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtiene información de un usuario por su ID (solo para ADMIN)
     * @param id ID del usuario
     * @return ResponseEntity con datos del usuario
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.get().getId());
        response.put("email", user.get().getEmail());
        response.put("role", user.get().getRole() != null ? 
                Map.of("id", user.get().getRole().getId(), "name", user.get().getRole().getName()) : 
                null);
        response.put("createdAt", user.get().getCreatedAt());
        response.put("updatedAt", user.get().getUpdatedAt());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Verifica si un email ya está en uso
     * @param email Email a verificar
     * @return ResponseEntity con resultado de verificación
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmailExists(@PathVariable String email) {
        boolean exists = userRepository.existsByEmail(email);
        
        Map<String, Boolean> response = Map.of("exists", exists);
        return ResponseEntity.ok(response);
    }
}