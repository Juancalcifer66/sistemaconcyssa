package com.concyssa.sistemaconcyssa.dto.orden;

import com.concyssa.sistemaconcyssa.enums.PrioridadOrden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "DTO de solicitud para la creación de una orden de trabajo")
public class OrdenTrabajoCreateDto {

    @NotBlank(message = "El codigo de la orden es obligatorio")
    @Schema(description = "Código único de la orden", example = "OT-2026-001")
    private String codigo;

    @NotBlank(message = "La descripcion es obligatoria")
    @Schema(description = "Detalle del trabajo a realizar", example = "Mantenimiento preventivo de bomba hidraulica")
    private String descripcion;

    @NotBlank(message = "La direccion u ubicacion es obligatoria")
    @Schema(description = "Ubicación o dirección de la intervención", example = "Av. Javier Prado Este 2465, San Borja")
    private String direccion;

    @NotNull(message = "La prioridad es obligatoria")
    @Schema(description = "Prioridad asignada a la orden", example = "ALTA")
    private PrioridadOrden prioridad;

    @Schema(description = "Fecha y hora programada para la atención", example = "2026-05-12T08:00:00")
    private LocalDateTime fechaProgramada;

    @Schema(description = "ID del operador asignado", example = "3")
    private Long operadorAsignadoId;
}

