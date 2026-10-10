package com.concyssa.sistemaconcyssa.dto.cloro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para registrar la medición de cloro")
public class MedicionCloroCreateDto {

    @Schema(description = "Número de sistema", example = "181")
    @NotNull(message = "El número de sistema es obligatorio")
    private Integer numeroSistema;

    @Schema(description = "Ubicación o punto de estación", example = "Rp-01")
    private String ubicacionPunto;

    @Schema(description = "Valor de cloro medido en mg/L", example = "1.25")
    @NotNull(message = "El valor de cloro es obligatorio")
    private Double valorCloroMgL;

    @Schema(description = "Observaciones de la medición", example = "Nivel dentro de los parámetros normales")
    private String observacion;
}