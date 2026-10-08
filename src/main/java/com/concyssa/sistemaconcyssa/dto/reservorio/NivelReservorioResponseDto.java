package com.concyssa.sistemaconcyssa.dto.reservorio;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta para el nivel de reservorios")
public class NivelReservorioResponseDto {

    @Schema(description = "ID del registro", example = "1")
    private Long id;

    @Schema(description = "Número de sistema", example = "1042")
    private Integer numeroSistema;

    @Schema(description = "Estación", example = "EST-SUR-01")
    private String estacion;

    @Schema(description = "Pasos llenos", example = "150")
    private Integer pasosLlenos;

    @Schema(description = "Pasos libres", example = "50")
    private Integer pasosLibres;

    @Schema(description = "Observación", example = "Sin novedades")
    private String observacion;

    @Schema(description = "Fecha y hora de registro", example = "2026-10-07T16:10:00")
    private LocalDateTime fechaRegistro;

    @Schema(description = "Nombre completo del controlador", example = "Juan Pérez")
    private String controladorNombre;
}