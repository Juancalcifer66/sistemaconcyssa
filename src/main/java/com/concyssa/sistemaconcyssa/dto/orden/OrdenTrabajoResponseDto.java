package com.concyssa.sistemaconcyssa.dto.orden;

import com.concyssa.sistemaconcyssa.enums.EstadoOrden;
import com.concyssa.sistemaconcyssa.enums.PrioridadOrden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con los datos de la orden de trabajo")
public class OrdenTrabajoResponseDto {

    @Schema(description = "ID de la orden de trabajo", example = "10")
    private Long id;

    @Schema(description = "Código único de la orden", example = "OT-2026-001")
    private String codigo;

    @Schema(description = "Descripción del trabajo", example = "Mantenimiento preventivo de bomba hidraulica")
    private String descripcion;

    @Schema(description = "Dirección o ubicación del trabajo", example = "Av. Javier Prado Este 2465, San Borja")
    private String direccion;

    @Schema(description = "Estado actual de la orden", example = "PENDIENTE")
    private EstadoOrden estado;

    @Schema(description = "Nivel de prioridad", example = "ALTA")
    private PrioridadOrden prioridad;

    @Schema(description = "Fecha y hora de creación de la orden", example = "2026-05-10T08:00:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha y hora programada", example = "2026-05-12T08:00:00")
    private LocalDateTime fechaProgramada;

    @Schema(description = "Username del usuario creador", example = "admin")
    private String creadorUsername;

    @Schema(description = "Username del operador asignado", example = "operador1")
    private String operadorAsignadoUsername;
}
