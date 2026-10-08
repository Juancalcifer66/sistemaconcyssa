package com.concyssa.sistemaconcyssa.dto.visita;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para registrar la visita de terceros")
public class VisitaTerceroCreateDto {

    @NotNull(message = "El número de sistema es obligatorio")
    @Schema(description = "Número de sistema", example = "4050")
    private Integer numeroSistema;

    @NotBlank(message = "La descripción es obligatoria")
    @Schema(description = "Motivo o descripción de la visita", example = "Inspección de medidores por empresa contratista externa")
    private String descripcion;

    @NotBlank(message = "Los datos de control son obligatorios")
    @Schema(description = "Datos de control (DNI, nombres, empresa)", example = "Juan Quispe - Contr. HydroS.A. - DNI 45879621")
    private String datosControl;
}