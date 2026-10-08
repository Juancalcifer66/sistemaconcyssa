package com.concyssa.sistemaconcyssa.dto.asistencia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para registrar la asistencia (ingreso)")
public class AsistenciaCreateDto {

    @Schema(description = "Observación o comentario del controlador", example = "Ingreso puntual al turno diurno")
    private String observacion;
}