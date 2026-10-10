package com.concyssa.sistemaconcyssa.dto.turno;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para la gestión y programación mensual de supervisores por zona y turno")
public class TurnoZonaCreateDto {

    @NotBlank(message = "El mes es obligatorio")
    @Schema(description = "Mes de programación", example = "Octubre 2026")
    private String mes;

    @NotBlank(message = "El nombre y apellido completo es obligatorio")
    @Schema(description = "Nombre y apellido completo del supervisor", example = "Carlos Alberto Mendoza Ramos")
    private String nombreCompleto;

    @NotBlank(message = "La zona es obligatoria")
    @Schema(description = "Zona de trabajo asignada (Zona Alta, Zona Baja, Zona Centro)", example = "Zona Alta")
    private String zona;

    @NotBlank(message = "El turno es obligatorio")
    @Schema(description = "Turno de trabajo (Mañana, Tarde, Noche)", example = "Mañana")
    private String turno;
}