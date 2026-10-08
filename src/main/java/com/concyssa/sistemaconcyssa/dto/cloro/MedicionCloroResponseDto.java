package com.concyssa.sistemaconcyssa.dto.cloro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con los datos de medición de cloro")
public class MedicionCloroResponseDto {

    @Schema(description = "ID del registro", example = "1")
    private Long id;

    @Schema(description = "Valor de cloro medido en mg/L", example = "1.25")
    private Double valorCloroMgL;

    @Schema(description = "Ubicación o punto de muestreo", example = "Reservorio R-3")
    private String ubicacionPunto;

    @Schema(description = "Observación registrada", example = "Nivel óptimo")
    private String observacion;

    @Schema(description = "Fecha y hora de la medición", example = "2026-10-07T15:00:00")
    private LocalDateTime fechaMedicion;

    @Schema(description = "Nombre completo del controlador", example = "Juan Pérez")
    private String controladorNombre;
}