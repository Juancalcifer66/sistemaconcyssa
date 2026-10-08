package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.error.ErrorResponseDto;
import com.concyssa.sistemaconcyssa.dto.orden.HistorialOrdenResponseDto;
import com.concyssa.sistemaconcyssa.service.OrdenTrabajoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Historial de Órdenes", description = "Puntos finales para consultar la trazabilidad y auditoría de cambios de estado")
@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "*")
public class HistorialOrdenController {

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;

    @Operation(
        summary = "Obtener el historial de cambios de una orden", 
        description = "Retorna la traza de auditoría y los cambios de estado sufridos por una orden de trabajo específica según su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Historial obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HistorialOrdenResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "No autorizado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Orden de trabajo no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(value = "{\n  \"timestamp\": \"2026-10-07T17:00:00Z\",\n  \"status\": 404,\n  \"error\": \"Not Found\",\n  \"message\": \"Orden de trabajo no encontrada con ID: 10\",\n  \"path\": \"/api/ordenes/10/historial\"\n}")
            )
        )
    })
    @GetMapping("/{id}/historial")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'SUPERVISOR')")
    public ResponseEntity<List<HistorialOrdenResponseDto>> obtenerHistorial(@PathVariable Long id) {
        List<HistorialOrdenResponseDto> historial = ordenTrabajoService.obtenerHistorialPorOrden(id);
        return ResponseEntity.ok(historial);
    }
}