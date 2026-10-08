package com.concyssa.sistemaconcyssa.dto.reservorio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para registrar el nivel de reservorios")
public class NivelReservorioCreateDto {

    @NotNull(message = "El número de sistema es obligatorio")
    @Schema(description = "Número de sistema (solo números)", example = "1042")
    private Integer numeroSistema;

    @NotBlank(message = "La estación es obligatoria")
    @Schema(description = "Estación (texto o guiones)", example = "EST-SUR-01")
    private String estacion;

    @NotNull(message = "Los pasos llenos son obligatorios")
    @Schema(description = "Cantidad de pasos llenos", example = "150")
    private Integer pasosLlenos;

    @NotNull(message = "Los pasos libres son obligatorios")
    @Schema(description = "Cantidad de pasos libres", example = "50")
    private Integer pasosLibres;

    @Schema(description = "Observación opcional", example = "Medición tomada sin novedades")
    private String observacion;
}