// src/main/java/com/hotel/auth_service/config/DBInitializer.java

package com.hotel.auth_service.config;

import com.hotel.auth_service.models.entity.Role;
import com.hotel.auth_service.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase se ejecuta automáticamente al iniciar la aplicación
 * para inicializar los roles predeterminados en la base de datos.
 */
@Component
public class DBInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        initRoles();
    }

    /**
     * Inicializa los roles predeterminados si no existen en la base de datos
     */
    private void initRoles() {
        if (roleRepository.count() == 0) {
            List<Role> roles = new ArrayList<>();
            
            roles.add(new Role("ADMIN"));
            roles.add(new Role("USER"));
            roles.add(new Role("HOTEL_MANAGER"));
            
            roleRepository.saveAll(roles);
            
            System.out.println("Roles predeterminados creados con éxito");
        } else {
            System.out.println("Los roles ya están inicializados");
        }
    }
}