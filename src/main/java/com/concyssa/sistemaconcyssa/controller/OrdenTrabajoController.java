package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.error.ErrorResponseDto;
import com.concyssa.sistemaconcyssa.dto.orden.ActualizarEstadoRequestDto;
import com.concyssa.sistemaconcyssa.dto.orden.OrdenTrabajoCreateDto;
import com.concyssa.sistemaconcyssa.dto.orden.OrdenTrabajoResponseDto;
import com.concyssa.sistemaconcyssa.enums.EstadoOrden;
import com.concyssa.sistemaconcyssa.enums.PrioridadOrden;
import com.concyssa.sistemaconcyssa.service.OrdenTrabajoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Órdenes de Trabajo", description = "Puntos finales para la gestión y seguimiento de órdenes de trabajo")
@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "*")
public class OrdenTrabajoController {

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;

    @Operation(summary = "Listar todas las órdenes de trabajo", description = "Obtiene una lista completa de todas las órdenes de trabajo registradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de órdenes de trabajo obtenida exitosamente"),
        @ApiResponse(
            responseCode = "401", 
            description = "No autorizado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(value = "{\n  \"timestamp\": \"2026-10-03T21:45:23Z\",\n  \"status\": 401,\n  \"error\": \"Unauthorized\",\n  \"message\": \"Acceso denegado. Se requiere un token válido.\",\n  \"path\": \"/api/ordenes\"\n}")
            )
        )
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'SUPERVISOR')")
    public ResponseEntity<List<OrdenTrabajoResponseDto>> listarOrdenes() {
        return ResponseEntity.ok(ordenTrabajoService.listarTodas());
    }

    @Operation(summary = "Listar órdenes de trabajo con filtros y paginación", description = "Permite filtrar las órdenes por estado, prioridad y operador asignado con soporte de paginación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de órdenes obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/filtrar")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'SUPERVISOR')")
    public ResponseEntity<Page<OrdenTrabajoResponseDto>> listarConFiltros(
            @RequestParam(required = false) EstadoOrden estado,
            @RequestParam(required = false) PrioridadOrden prioridad,
            @RequestParam(required = false) Long operadorId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ordenTrabajoService.listarConFiltros(estado, prioridad, operadorId, pageable));
    }

    @Operation(summary = "Obtener una orden de trabajo por ID", description = "Retorna la información detallada de una orden de trabajo específica según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden de trabajo encontrada exitosamente"),
        @ApiResponse(
            responseCode = "404", 
            description = "Orden de trabajo no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(value = "{\n  \"timestamp\": \"2026-10-03T21:45:23Z\",\n  \"status\": 404,\n  \"error\": \"Not Found\",\n  \"message\": \"La orden de trabajo con ID 50 no fue encontrada.\",\n  \"path\": \"/api/ordenes/50\"\n}")
            )
        )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'SUPERVISOR')")
    public ResponseEntity<OrdenTrabajoResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenTrabajoService.obtenerPorId(id));
    }

    @Operation(summary = "Crear una nueva orden de trabajo", description = "Registra una nueva orden de trabajo en el sistema asignando el usuario creador a partir de su DNI.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Orden de trabajo creada exitosamente"),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(value = "{\n  \"timestamp\": \"2026-10-03T21:45:23Z\",\n  \"status\": 400,\n  \"error\": \"Bad Request\",\n  \"message\": \"La descripción y la ubicación son requeridas.\",\n  \"path\": \"/api/ordenes\"\n}")
            )
        )
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<OrdenTrabajoResponseDto> crearOrden(
            @Valid @RequestBody OrdenTrabajoCreateDto dto,
            Authentication authentication) {
        String dniCreador = authentication.getName();
        return new ResponseEntity<>(ordenTrabajoService.crearOrden(dto, dniCreador), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar completamente una orden de trabajo", description = "Permite modificar los datos generales de una orden existente por ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Orden de trabajo no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<OrdenTrabajoResponseDto> actualizarOrden(
            @PathVariable Long id,
            @Valid @RequestBody OrdenTrabajoCreateDto dto) {
        return ResponseEntity.ok(ordenTrabajoService.actualizarOrden(id, dto));
    }

    @Operation(summary = "Actualizar estado de una orden de trabajo", description = "Permite cambiar el estado de una orden (PENDIENTE, EN_PROCESO, ATENDIDA, CANCELADA). Requiere rol OPERADOR, SUPERVISOR o ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Estado de la orden actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = OrdenTrabajoResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de solicitud inválidos o estado no reconocido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)
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
            description = "Orden de trabajo o usuario no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)
            )
        )
    })
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'OPERADOR')")
    public ResponseEntity<OrdenTrabajoResponseDto> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoRequestDto dto,
            Authentication authentication) {

        String dniAccion = authentication.getName();

        OrdenTrabajoResponseDto ordenActualizada = ordenTrabajoService.actualizarEstado(
                id,
                dto.getNuevoEstado(),
                dto.getObservacion(),
                dniAccion
        );

        return ResponseEntity.ok(ordenActualizada);
    }

    @Operation(summary = "Eliminar una orden de trabajo", description = "Elimina una orden de trabajo por su ID. Restringido exclusivamente al rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Orden de trabajo eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Orden de trabajo no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Long id) {
        ordenTrabajoService.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }
}