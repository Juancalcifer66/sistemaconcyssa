package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.turno.TurnoZonaCreateDto;
import com.concyssa.sistemaconcyssa.dto.turno.TurnoZonaResponseDto;
import com.concyssa.sistemaconcyssa.service.TurnoZonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/programacion-supervisores")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ADMIN', 'SUPERVISOR')")
@RequiredArgsConstructor
@Tag(name = "Programación de Supervisores", description = "Gestión de turnos y zonas por mes para Administradores y Supervisores")
public class TurnoZonaController {

    private final TurnoZonaService service;

    @Operation(summary = "Obtener asignaciones por mes")
    @GetMapping("/mes/{mes}")
    public ResponseEntity<List<TurnoZonaResponseDto>> obtenerPorMes(@PathVariable String mes) {
        return ResponseEntity.ok(service.listarPorMes(mes));
    }

    @Operation(summary = "Registrar nueva asignación de supervisor")
    @PostMapping
    public ResponseEntity<TurnoZonaResponseDto> crear(@Valid @RequestBody TurnoZonaCreateDto dto) {
        return new ResponseEntity<>(service.guardarProgramacion(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Modificar mes, zona o turno de supervisor")
    @PutMapping("/{id}")
    public ResponseEntity<TurnoZonaResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TurnoZonaCreateDto dto) {
        return ResponseEntity.ok(service.actualizarProgramacion(id, dto));
    }
}