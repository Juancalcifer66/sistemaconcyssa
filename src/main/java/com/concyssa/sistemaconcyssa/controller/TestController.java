package com.concyssa.sistemaconcyssa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pruebas y Roles", description = "Endpoints de verificación de permisos por roles")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/protegido")
    public ResponseEntity<String> accesoProtegido() {
        return ResponseEntity.ok("Acceso concedido: Ruta protegida con JWT.");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> accesoAdmin() {
        return ResponseEntity.ok("Acceso concedido: Endpoint de Administrador.");
    }

    @GetMapping("/operador")
    @PreAuthorize("hasAuthority('ROLE_OPERADOR')")
    public ResponseEntity<String> accesoOperador() {
        return ResponseEntity.ok("Acceso concedido: Endpoint de Operador.");
    }
}
