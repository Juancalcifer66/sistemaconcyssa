package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.reservorio.NivelReservorioCreateDto;
import com.concyssa.sistemaconcyssa.dto.reservorio.NivelReservorioResponseDto;
import com.concyssa.sistemaconcyssa.service.NivelReservorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/reservorios")
@RequiredArgsConstructor
@Tag(name = "Nivel de Reservorios", description = "Endpoints para el registro de niveles de reservorios (número de sistema, estación, pasos)")
public class NivelReservorioController {

    private final NivelReservorioService nivelReservorioService;

    @Operation(summary = "Registrar nivel de reservorio", description = "Permite al controlador registrar los pasos llenos y libres por número de sistema y estación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nivel registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping
    @PreAuthorize("hasRole('CONTROLADOR')")
    public ResponseEntity<NivelReservorioResponseDto> registrarNivel(
            @Valid @RequestBody NivelReservorioCreateDto dto,
            Authentication authentication) {

        String dniControlador = authentication.getName();
        NivelReservorioResponseDto response = nivelReservorioService.registrarNivel(dto, dniControlador);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los registros de reservorios", description = "Permite a supervisores y administradores consultar el historial general.")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<NivelReservorioResponseDto>> listarTodos() {
        return ResponseEntity.ok(nivelReservorioService.listarTodos());
    }

    @Operation(summary = "Listar por controlador", description = "Permite consultar los registros de reservorios de un controlador específico.")
    @GetMapping("/controlador/{controladorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'CONTROLADOR')")
    public ResponseEntity<List<NivelReservorioResponseDto>> listarPorControlador(@PathVariable Long controladorId) {
        return ResponseEntity.ok(nivelReservorioService.listarPorControlador(controladorId));
    }
}