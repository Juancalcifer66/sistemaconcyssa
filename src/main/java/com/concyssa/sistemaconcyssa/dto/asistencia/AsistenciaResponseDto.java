package com.concyssa.sistemaconcyssa.dto.asistencia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con los datos de asistencia")
public class AsistenciaResponseDto {

    @Schema(description = "ID del registro de asistencia", example = "1")
    private Long id;

    @Schema(description = "Fecha y hora automática de ingreso", example = "2026-10-07T08:00:00")
    private LocalDateTime fechaIngreso;

    @Schema(description = "Fecha y hora de salida", example = "2026-10-07T16:00:00")
    private LocalDateTime fechaSalida;

    @Schema(description = "Ruta o nombre de archivo de la foto almacenada", example = "uuid-foto.jpg")
    private String fotoUrl;

    @Schema(description = "Observación registrada", example = "Ingreso puntual")
    private String observacion;

    @Schema(description = "Nombre completo del controlador", example = "Juan Pérez")
    private String controladorNombre;
}