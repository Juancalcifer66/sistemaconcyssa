package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.reservorio.NivelReservorioCreateDto;
import com.concyssa.sistemaconcyssa.entity.NivelReservorio;
import com.concyssa.sistemaconcyssa.repository.NivelReservorioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservorios")
public class NivelReservorioController {

    @Autowired
    private NivelReservorioRepository reservorioRepository;

    // Endpoint para registrar nivel (Solo para Controladores y Administradores)
    @PostMapping("/niveles")
    @PreAuthorize("hasAnyRole('CONTROLADOR', 'ADMIN')")
    public ResponseEntity<?> registrarNivel(@Valid @RequestBody NivelReservorioCreateDto dto) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioActual = (authentication != null && authentication.isAuthenticated()) 
                ? authentication.getName() 
                : "Sistema";

        NivelReservorio nivel = NivelReservorio.builder()
                .numeroSistema(dto.getNumeroSistema())
                .estacion(dto.getEstacion())
                .pasosLlenos(dto.getPasosLlenos())
                .pasosLibres(dto.getPasosLibres())
                .observacion(dto.getObservacion())
                .build();

        reservorioRepository.save(nivel);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Nivel de reservorio registrado exitosamente.");
    }

    // Endpoint para listar todos los registros históricos de niveles
    @GetMapping("/niveles")
    @PreAuthorize("hasAnyRole('CONTROLADOR', 'ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<NivelReservorio>> listarNiveles() {
        List<NivelReservorio> lista = reservorioRepository.findAll();
        return ResponseEntity.ok(lista);
    }
}