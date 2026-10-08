package com.concyssa.sistemaconcyssa.dto.danio;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para reportar un daño o falla en campo")
public class DanioFallaCreateDto {

    @Schema(description = "Descripción detallada del caso o incidencia", example = "Fuga de agua detectada en la válvula principal del sector 4")
    private String descripcion;
}