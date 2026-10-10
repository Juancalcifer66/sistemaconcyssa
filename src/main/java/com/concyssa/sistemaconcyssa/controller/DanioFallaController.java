package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.danio.DanioFallaCreateDto;
import com.concyssa.sistemaconcyssa.dto.danio.DanioFallaResponseDto;
import com.concyssa.sistemaconcyssa.service.DanioFallaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/danos")
@RequiredArgsConstructor
@Tag(name = "Daños y Fallas", description = "Endpoints para el reporte de incidencias y fallas operativas con evidencia fotográfica")
public class DanioFallaController {

    private final DanioFallaService danioFallaService;

    @Operation(summary = "Registrar daño o falla", description = "Permite al controlador reportar un daño o falla enviando la información en formato JSON.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte de daño registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping
    @PreAuthorize("hasRole('CONTROLADOR')")
    public ResponseEntity<DanioFallaResponseDto> registrarDanio(
            @Valid @RequestBody DanioFallaCreateDto dto,
            Authentication authentication) {

        String dniControlador = authentication.getName();
        // Pasamos null en la foto temporalmente mientras pruebas el JSON
        DanioFallaResponseDto response = danioFallaService.registrarDanio(dto, null, dniControlador);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los daños y fallas", description = "Permite a supervisores y administradores consultar todos los reportes registrados.")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<DanioFallaResponseDto>> listarTodos() {
        return ResponseEntity.ok(danioFallaService.listarTodos());
    }

    @Operation(summary = "Listar daños por controlador", description = "Permite consultar los reportes emitidos por un controlador específico.")
    @GetMapping("/controlador/{controladorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'CONTROLADOR')")
    public ResponseEntity<List<DanioFallaResponseDto>> listarPorControlador(@PathVariable Long controladorId) {
        return ResponseEntity.ok(danioFallaService.listarPorControlador(controladorId));
    }
}