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
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
@Tag(name = "Cronograma de Turnos y Zonas", description = "Gestión y consulta de turnos mensuales para supervisores y controladores")
public class TurnoZonaController {

    private final TurnoZonaService service;

    @Operation(summary = "Asignar turno y zona (Solo Admin/Supervisor)")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<TurnoZonaResponseDto> asignarTurno(@Valid @RequestBody TurnoZonaCreateDto dto) {
        return new ResponseEntity<>(service.asignarTurno(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar cronograma mensual (Solo lectura para Supervisores y Controladores)")
    @GetMapping("/mes")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'CONTROLADOR')")
    public ResponseEntity<List<TurnoZonaResponseDto>> listarPorMes(
            @RequestParam int anio,
            @RequestParam int mes) {
        return ResponseEntity.ok(service.listarPorMes(anio, mes));
    }

    @Operation(summary = "Listar todo el cronograma histórico")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<TurnoZonaResponseDto>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}