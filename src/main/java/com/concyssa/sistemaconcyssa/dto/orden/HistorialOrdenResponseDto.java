package com.concyssa.sistemaconcyssa.dto.orden;

import com.concyssa.sistemaconcyssa.enums.EstadoOrden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con el historial de cambios de estado de una orden")
public class HistorialOrdenResponseDto {

    @Schema(description = "ID del registro de historial", example = "1")
    private Long id;

    @Schema(description = "ID de la orden de trabajo asociada", example = "10")
    private Long ordenId;

    @Schema(description = "Nombre de usuario que realizó el cambio", example = "jgarcia")
    private String usuarioUsername;

    @Schema(description = "Estado anterior de la orden", example = "PENDIENTE")
    private EstadoOrden estadoAnterior;

    @Schema(description = "Nuevo estado asignado", example = "EN_PROCESO")
    private EstadoOrden estadoNuevo;

    @Schema(description = "Observaciones o comentarios del cambio", example = "Se inicia la atención en campo")
    private String observacion;

    @Schema(description = "Fecha y hora en que se registró el cambio", example = "2026-05-10T09:30:00")
    private LocalDateTime fechaCambio;
}
