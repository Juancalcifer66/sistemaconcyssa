package com.concyssa.sistemaconcyssa.config;

import com.concyssa.sistemaconcyssa.entity.Rol;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.enums.RolNombre;
import com.concyssa.sistemaconcyssa.repository.RolRepository;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, 
                                   RolRepository rolRepository, 
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            String dniAdmin = "12345678";
            
            // Buscar si el admin ya existe, si no, crear una instancia nueva
            Usuario admin = usuarioRepository.findByDni(dniAdmin).orElse(new Usuario());
            
            if (admin.getId() == null) {
                admin.setDni(dniAdmin);
                admin.setEmail("admin@concyssa.com");
                admin.setNombreCompleto("Administrador SICO");
                admin.setEstado(true);
            }
            
            // Forzar siempre la actualización de la contraseña cifrada con BCrypt
            admin.setPassword(passwordEncoder.encode("12345678"));

            // Asegurar que el rol ROLE_ADMIN exista y asignarlo
            Rol rolAdmin = rolRepository.findByNombre(RolNombre.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Rol nuevoRol = new Rol();
                        nuevoRol.setNombre(RolNombre.ROLE_ADMIN);
                        return rolRepository.save(nuevoRol);
                    });
                    
            admin.setRoles(new HashSet<>(Collections.singletonList(rolAdmin)));

            usuarioRepository.save(admin);
            System.out.println("--> Usuario administrador inicializado y contraseña cifrada con BCrypt correctamente.");
        };
    }
}