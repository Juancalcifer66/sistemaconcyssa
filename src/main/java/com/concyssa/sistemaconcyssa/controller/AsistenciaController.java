package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.asistencia.AsistenciaCreateDto;
import com.concyssa.sistemaconcyssa.dto.asistencia.AsistenciaResponseDto;
import com.concyssa.sistemaconcyssa.service.AsistenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/asistencias")
@RequiredArgsConstructor
@Tag(name = "Asistencia", description = "Endpoints para la gestión de asistencia de controladores con evidencia fotográfica")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @Operation(summary = "Registrar ingreso de asistencia", description = "Permite al controlador registrar su ingreso con foto y observación opcional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ingreso registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o ya existe un turno abierto"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CONTROLADOR')")
    public ResponseEntity<AsistenciaResponseDto> registrarIngreso(
            @RequestPart("asistencia") AsistenciaCreateDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto,
            Authentication authentication) {
        
        String dniControlador = authentication.getName(); // El DNI está almacenado en el username del token JWT
        AsistenciaResponseDto response = asistenciaService.registrarIngreso(dto, foto, dniControlador);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Registrar salida de asistencia", description = "Registra la hora de salida automática para el turno activo del controlador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salida registrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró un turno abierto"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PatchMapping("/salida")
    @PreAuthorize("hasRole('CONTROLADOR')")
    public ResponseEntity<AsistenciaResponseDto> registrarSalida(Authentication authentication) {
        String dniControlador = authentication.getName();
        AsistenciaResponseDto response = asistenciaService.registrarSalida(dniControlador);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todas las asistencias", description = "Permite a los supervisores o administradores listar todos los registros de asistencia.")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<AsistenciaResponseDto>> listarTodas() {
        return ResponseEntity.ok(asistenciaService.listarTodas());
    }

    @Operation(summary = "Listar asistencias por controlador", description = "Permite consultar los registros de asistencia de un controlador específico mediante su ID.")
    @GetMapping("/controlador/{controladorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'CONTROLADOR')")
    public ResponseEntity<List<AsistenciaResponseDto>> listarPorControlador(@PathVariable Long controladorId) {
        return ResponseEntity.ok(asistenciaService.listarPorControlador(controladorId));
    }
}