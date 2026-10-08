package com.concyssa.sistemaconcyssa.dto.cloro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para registrar la medición de cloro")
public class MedicionCloroCreateDto {

    @NotNull(message = "El valor de cloro en mg/L es obligatorio")
    @Schema(description = "Valor decimal de cloro medido en mg/L", example = "1.25")
    private Double valorCloroMgL;

    @Schema(description = "Ubicación o punto de muestreo", example = "Reservorio R-3 / Válvula de salida")
    private String ubicacionPunto;

    @Schema(description = "Observación opcional", example = "Nivel óptimo dentro del rango permitido")
    private String observacion;
}