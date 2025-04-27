// src/main/java/com/hotel/auth_service/service/RefreshTokenService.java

package com.hotel.auth_service.service;

import com.hotel.auth_service.models.entity.RefreshToken;
import com.hotel.auth_service.models.entity.User;
import com.hotel.auth_service.repositories.RefreshTokenRepository;
import com.hotel.auth_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    
    @Value("${app.refreshTokenExpirationMs}")
    private long refreshTokenDurationMs;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Encuentra un refresh token por su valor
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    /**
     * Crea un nuevo refresh token para un usuario
     */
    public RefreshToken createRefreshToken(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con id: " + userId);
        }
        
        // Eliminar tokens existentes para el usuario
        refreshTokenRepository.deleteByUser(userOpt.get());
        
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userOpt.get());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + refreshTokenDurationMs));
        
        return refreshTokenRepository.save(refreshToken);
    }
    
    /**
     * Verifica si un token ha expirado y devuelve el token si es válido
     * @param token el token a verificar
     * @return el mismo token si no está expirado
     * @throws RuntimeException si el token está expirado
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expirado. Por favor inicie sesión nuevamente");
        }
        
        return token;
    }
    
    /**
     * Elimina un refresh token por userId
     */
    public void deleteByUserId(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        userOpt.ifPresent(user -> refreshTokenRepository.deleteByUser(user));
    }
}