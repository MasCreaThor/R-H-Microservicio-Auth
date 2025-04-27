// src/main/java/com/hotel/auth_service/security/jwt/AuthEntryPointJwt.java

package com.hotel.auth_service.security.jwt;

import java.io.IOException;

// Cambiar de javax.servlet a jakarta.servlet
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        logger.error("Error de autenticación no autorizada: {}", authException.getMessage());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), 
            mapper.createObjectNode()
                .put("status", HttpServletResponse.SC_UNAUTHORIZED)
                .put("error", "No autorizado")
                .put("message", authException.getMessage() != null ? 
                    authException.getMessage() : "Token inválido o expirado")
                .put("path", request.getServletPath())
        );
    }
}