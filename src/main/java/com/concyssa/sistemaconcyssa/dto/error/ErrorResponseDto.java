package com.concyssa.sistemaconcyssa.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura estándar para respuestas de error de la API")
public class ErrorResponseDto {

    @Schema(description = "Fecha y hora en que ocurrió el error", example = "2026-10-03T16:05:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Nombre del estado HTTP", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje descriptivo del error", example = "Los datos ingresados no son válidos")
    private String message;

    @Schema(description = "Ruta de la solicitud HTTP", example = "/api/ordenes")
    private String path;

    @Schema(description = "Detalles adicionales de los errores (útil para validaciones de campos)")
    private Map<String, String> details;
}
