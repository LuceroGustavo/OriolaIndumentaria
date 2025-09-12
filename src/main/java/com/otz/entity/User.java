package com.otz.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @NotBlank(message = "El nombre de usuario es requerido")
    @Column(unique = true, nullable = false)
    public String username;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "El formato del email no es válido")
    @Column(unique = true, nullable = false)
    public String email;
    
    @NotBlank(message = "La contraseña es requerida")
    @Column(nullable = false)
    public String password;
    
    @Column(name = "full_name")
    public String fullName;
    
    @Column(name = "is_active")
    public Boolean isActive = true;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    public Role role = Role.ADMIN;
    
    public enum Role {
        ADMIN, USER
    }
}
