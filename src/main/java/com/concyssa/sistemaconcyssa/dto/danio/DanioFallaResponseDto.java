package com.concyssa.sistemaconcyssa.dto.danio;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con los datos del reporte de daño o falla")
public class DanioFallaResponseDto {

    @Schema(description = "ID del reporte", example = "1")
    private Long id;

    @Schema(description = "Descripción detallada del caso", example = "Fuga de agua detectada...")
    private String descripcion;

    @Schema(description = "Nombre o ruta de la foto adjunta", example = "uuid-foto-danio.jpg")
    private String fotoUrl;

    @Schema(description = "Fecha y hora en que se realizó el reporte", example = "2026-10-07T14:30:00")
    private LocalDateTime fechaReporte;

    @Schema(description = "Nombre completo del controlador que reportó", example = "Juan Pérez")
    private String controladorNombre;
}