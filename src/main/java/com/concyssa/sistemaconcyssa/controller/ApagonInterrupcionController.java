package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.apagon.ApagonCreateDto;
import com.concyssa.sistemaconcyssa.dto.apagon.ApagonResponseDto;
import com.concyssa.sistemaconcyssa.service.ApagonInterrupcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apagones")
@RequiredArgsConstructor
@Tag(name = "Apagones e Interrupciones", description = "Endpoints para el registro de cortes o fallas de energía")
public class ApagonInterrupcionController {

    private final ApagonInterrupcionService service;

    @Operation(summary = "Registrar apagón o interrupción")
    @PostMapping
    @PreAuthorize("hasRole('CONTROLADOR')")
    public ResponseEntity<ApagonResponseDto> registrar(@Valid @RequestBody ApagonCreateDto dto, Authentication auth) {
        return new ResponseEntity<>(service.registrarApagon(dto, auth.getName()), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los apagones")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<ApagonResponseDto>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Listar apagones por controlador")
    @GetMapping("/controlador/{controladorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'CONTROLADOR')")
    public ResponseEntity<List<ApagonResponseDto>> listarPorControlador(@PathVariable Long controladorId) {
        return ResponseEntity.ok(service.listarPorControlador(controladorId));
    }
}