// src/main/java/com/hotel/auth_service/controllers/PeopleController.java

package com.hotel.auth_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.hotel.auth_service.models.dto.PeopleRequest;
import com.hotel.auth_service.models.entity.People;
import com.hotel.auth_service.models.entity.User;
import com.hotel.auth_service.repositories.PeopleRepository;
import com.hotel.auth_service.repositories.UserRepository;
import com.hotel.auth_service.security.jwt.UserDetailsImpl;

// Cambiar de javax.validation a jakarta.validation
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/people")
public class PeopleController {
    
    @Autowired
    private PeopleRepository peopleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Obtiene información personal del usuario autenticado
     * @return ResponseEntity con datos personales
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyPeopleInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        Optional<User> userOpt = userRepository.findById(userDetails.getId());
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Optional<People> peopleOpt = peopleRepository.findByUser(userOpt.get());
        
        if (peopleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(peopleOpt.get());
    }
    
    /**
     * Actualiza información personal del usuario autenticado
     * @param peopleRequest Datos a actualizar
     * @return ResponseEntity con datos actualizados
     */
    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePeopleInfo(@Valid @RequestBody PeopleRequest peopleRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        Optional<User> userOpt = userRepository.findById(userDetails.getId());
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Optional<People> peopleOpt = peopleRepository.findByUser(userOpt.get());
        
        People people;
        if (peopleOpt.isEmpty()) {
            // Crear nuevo registro si no existe
            people = new People();
            people.setUser(userOpt.get());
        } else {
            people = peopleOpt.get();
        }
        
        // Actualizar campos
        people.setNombre(peopleRequest.getNombre());
        people.setApellido(peopleRequest.getApellido());
        people.setTelefono(peopleRequest.getTelefono());
        
        People updatedPeople = peopleRepository.save(people);
        
        return ResponseEntity.ok(updatedPeople);
    }
    
    /**
     * Obtiene información personal por ID de usuario (solo ADMIN)
     * @param userId ID del usuario
     * @return ResponseEntity con datos personales
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getPeopleByUserId(@PathVariable String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Optional<People> peopleOpt = peopleRepository.findByUser(userOpt.get());
        
        if (peopleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(peopleOpt.get());
    }
    
    /**
     * Obtiene todas las personas (solo ADMIN)
     * @return Lista de personas
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<People> getAllPeople() {
        return peopleRepository.findAll();
    }
}