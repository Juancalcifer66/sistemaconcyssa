package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.cloro.MedicionCloroCreateDto;
import com.concyssa.sistemaconcyssa.dto.cloro.MedicionCloroResponseDto;
import com.concyssa.sistemaconcyssa.service.MedicionCloroService;
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
@RequestMapping("/api/cloro")
@RequiredArgsConstructor
@Tag(name = "Medición de Cloro", description = "Endpoints para el registro y consulta de los niveles de cloro (mg/L) en campo")
public class MedicionCloroController {

    private final MedicionCloroService medicionCloroService;

    @Operation(summary = "Registrar medición de cloro", description = "Permite al controlador registrar el valor decimal de cloro en mg/L.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medición registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping
    @PreAuthorize("hasRole('CONTROLADOR')")
    public ResponseEntity<MedicionCloroResponseDto> registrarMedicion(
            @Valid @RequestBody MedicionCloroCreateDto dto,
            Authentication authentication) {

        String dniControlador = authentication.getName();
        MedicionCloroResponseDto response = medicionCloroService.registrarMedicion(dto, dniControlador);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las mediciones de cloro", description = "Permite a supervisores y administradores consultar el historial general de mediciones.")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<MedicionCloroResponseDto>> listarTodas() {
        return ResponseEntity.ok(medicionCloroService.listarTodas());
    }

    @Operation(summary = "Listar mediciones por controlador", description = "Permite consultar las mediciones registradas por un controlador específico.")
    @GetMapping("/controlador/{controladorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'CONTROLADOR')")
    public ResponseEntity<List<MedicionCloroResponseDto>> listarPorControlador(@PathVariable Long controladorId) {
        return ResponseEntity.ok(medicionCloroService.listarPorControlador(controladorId));
    }
}