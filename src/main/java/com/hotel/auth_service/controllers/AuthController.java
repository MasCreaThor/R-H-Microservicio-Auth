// src/main/java/com/hotel/auth_service/controllers/AuthController.java

package com.hotel.auth_service.controllers;

import java.util.Optional;

// Cambiar de javax.validation a jakarta.validation
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.auth_service.models.dto.JwtResponse;
import com.hotel.auth_service.models.dto.LoginRequest;
import com.hotel.auth_service.models.dto.MessageResponse;
import com.hotel.auth_service.models.dto.PeopleRequest;
import com.hotel.auth_service.models.dto.RegisterRequest;
import com.hotel.auth_service.models.dto.TokenRefreshRequest;
import com.hotel.auth_service.models.dto.TokenRefreshResponse;
import com.hotel.auth_service.models.entity.People;
import com.hotel.auth_service.models.entity.RefreshToken;
import com.hotel.auth_service.models.entity.Role;
import com.hotel.auth_service.models.entity.User;
import com.hotel.auth_service.repositories.PeopleRepository;
import com.hotel.auth_service.repositories.RoleRepository;
import com.hotel.auth_service.repositories.UserRepository;
import com.hotel.auth_service.security.jwt.JwtUtils;
import com.hotel.auth_service.security.jwt.UserDetailsImpl;
import com.hotel.auth_service.service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PeopleRepository peopleRepository;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private RefreshTokenService refreshTokenService;
    
    /**
     * Endpoint para registro de nuevos usuarios
     * @param registerRequest Datos de registro
     * @return ResponseEntity con mensaje de éxito o error
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El email ya está en uso!"));
        }
        
        // Verificar que las contraseñas coincidan
        if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Las contraseñas no coinciden!"));
        }
        
        // Crear nuevo usuario
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        
        // Asignar rol
        Role userRole;
        if (registerRequest.getRoleId() != null) {
            Optional<Role> roleOpt = roleRepository.findById(registerRequest.getRoleId());
            if (roleOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Rol no encontrado!"));
            }
            userRole = roleOpt.get();
        } else {
            // Por defecto se asigna rol USER
            userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
        }
        
        user.setRole(userRole);
        User savedUser = userRepository.save(user);
        
        // Crear información personal si se proporciona
        if (registerRequest.getPeopleInfo() != null) {
            PeopleRequest peopleInfo = registerRequest.getPeopleInfo();
            People people = new People();
            people.setUser(savedUser);
            people.setNombre(peopleInfo.getNombre());
            people.setApellido(peopleInfo.getApellido());
            people.setTelefono(peopleInfo.getTelefono());
            
            peopleRepository.save(people);
        }
        
        // Autenticar usuario después de registro
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPasswordConfirm()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getRole().getName()));
    }
    
    /**
     * Endpoint para login de usuarios
     * @param loginRequest Credenciales de login
     * @return ResponseEntity con tokens y datos de usuario
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getRole().getName()));
    }
    
    /**
     * Endpoint para renovar token de acceso
     * @param request Solicitud con refresh token
     * @return ResponseEntity con nuevo token de acceso y refresh token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        
        try {
            // Intentar encontrar el token de refresco
            Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findByToken(requestRefreshToken);
            
            if (refreshTokenOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Refresh token no encontrado en la base de datos"));
            }
            
            // Verificar si el token ha expirado
            RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenOpt.get());
            
            // Generar nuevo token de acceso
            User user = refreshToken.getUser();
            String token = jwtUtils.generateTokenFromUser(user);
            
            return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse(e.getMessage()));
        }
    }
    
    /**
     * Endpoint para validar token
     * @param token Token a validar
     * @return ResponseEntity con resultado de validación
     */
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        boolean isValid = jwtUtils.validateJwtToken(token);
        return ResponseEntity.ok(isValid);
    }
    
    /**
     * Endpoint para cerrar sesión
     * @param userId ID del usuario
     * @return ResponseEntity con mensaje de éxito
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody String userId) {
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}