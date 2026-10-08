package com.concyssa.sistemaconcyssa.dto.turno;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "DTO para asignar un turno y zona a un controlador")
public class TurnoZonaCreateDto {

    @NotNull(message = "La fecha es obligatoria")
    @Schema(description = "Fecha del cronograma", example = "2026-10-08")
    private LocalDate fecha;

    @NotNull(message = "El turno es obligatorio")
    @Schema(description = "Turno de trabajo", example = "MAÑANA")
    private String turno;

    @NotNull(message = "La zona es obligatoria")
    @Schema(description = "Zona o estación asignada", example = "Reservorio Sur - Sector 2")
    private String zona;

    @NotNull(message = "El ID del controlador es obligatorio")
    @Schema(description = "ID del usuario controlador", example = "3")
    private Long controladorId;
}