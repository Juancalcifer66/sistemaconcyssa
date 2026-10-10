package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.visita.VisitaTerceroCreateDto;
import com.concyssa.sistemaconcyssa.dto.visita.VisitaTerceroResponseDto;
import com.concyssa.sistemaconcyssa.service.VisitaTerceroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Visitas de Terceros", description = "Endpoints para el registro y consulta de visitas de terceros en estaciones")
@RestController
@RequestMapping("/api/visitas")
@RequiredArgsConstructor
public class VisitaTerceroController {

    private final VisitaTerceroService service;

    @Operation(summary = "Registrar visita de terceros")
    @PostMapping
    @PreAuthorize("hasRole('CONTROLADOR')")
    public ResponseEntity<VisitaTerceroResponseDto> registrar(
            @RequestBody VisitaTerceroCreateDto dto,
            Authentication auth) {
        
        // Llamamos al servicio pasando null en la foto
        return new ResponseEntity<>(service.registrarVisita(dto, null, auth.getName()), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las visitas de terceros")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'CONTROLADOR')")
    public ResponseEntity<List<VisitaTerceroResponseDto>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Listar visitas por ID de controlador")
    @GetMapping("/controlador/{controladorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'CONTROLADOR')")
    public ResponseEntity<List<VisitaTerceroResponseDto>> listarPorControlador(@PathVariable Long controladorId) {
        return ResponseEntity.ok(service.listarPorControlador(controladorId));
    }
}