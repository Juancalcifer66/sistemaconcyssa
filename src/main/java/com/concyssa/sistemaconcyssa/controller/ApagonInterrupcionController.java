package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.apagon.ApagonCreateDto;
import com.concyssa.sistemaconcyssa.dto.apagon.ApagonResponseDto;
import com.concyssa.sistemaconcyssa.service.ApagonInterrupcionService;
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
@RequestMapping("/api/apagones")
@RequiredArgsConstructor
@Tag(name = "Apagones e Interrupciones Eléctricas", description = "Endpoints para el registro y control de apagones e incidencias eléctricas en campo")
public class ApagonInterrupcionController {

    private final ApagonInterrupcionService apagonInterrupcionService;

    @Operation(summary = "Registrar apagón o interrupción", description = "Permite al controlador reportar un corte o interrupción eléctrica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Apagón registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping
    @PreAuthorize("hasRole('CONTROLADOR')")
    public ResponseEntity<ApagonResponseDto> registrarApagon(
            @Valid @RequestBody ApagonCreateDto dto,
            Authentication authentication) {

        String dniControlador = authentication.getName();
        ApagonResponseDto response = apagonInterrupcionService.registrarApagon(dto, dniControlador);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los apagones", description = "Permite a supervisores y administradores consultar el historial de apagones.")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<ApagonResponseDto>> listarTodos() {
        return ResponseEntity.ok(apagonInterrupcionService.listarTodos());
    }

    @Operation(summary = "Listar apagones por controlador", description = "Permite consultar los reportes eléctricos emitidos por un controlador específico.")
    @GetMapping("/controlador/{controladorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'CONTROLADOR')")
    public ResponseEntity<List<ApagonResponseDto>> listarPorControlador(@PathVariable Long controladorId) {
        return ResponseEntity.ok(apagonInterrupcionService.listarPorControlador(controladorId));
    }
}