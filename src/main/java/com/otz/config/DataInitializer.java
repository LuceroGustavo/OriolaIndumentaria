package com.otz.config;

import com.otz.entity.User;
import com.otz.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existe un usuario administrador
        if (userRepository.findByUsername("admin").isEmpty()) {
            // Crear usuario administrador por defecto
            User admin = new User();
            admin.username = "admin";
            admin.email = "admin@oriola.com";
            admin.password = passwordEncoder.encode("admin");
            admin.fullName = "Administrador ORIOLA";
            admin.isActive = true;
            admin.role = User.Role.ADMIN;
            
            userRepository.save(admin);
            System.out.println("✅ Usuario administrador creado: admin/admin");
        } else {
            System.out.println("ℹ️ Usuario administrador ya existe");
        }
    }
}
