package com.concyssa.sistemaconcyssa.dto.apagon;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para registrar un apagón o interrupción")
public class ApagonCreateDto {

    @NotNull(message = "El número de sistema es obligatorio")
    @Schema(description = "Número de sistema (solo números)", example = "3021")
    private Integer numeroSistema;

    @NotBlank(message = "La descripción es obligatoria")
    @Schema(description = "Descripción detallada del apagón o interrupción", example = "Corte imprevisto del suministro eléctrico en la estación de bombeo")
    private String descripcion;
}