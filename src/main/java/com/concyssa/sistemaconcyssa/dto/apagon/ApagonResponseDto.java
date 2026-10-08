package com.concyssa.sistemaconcyssa.dto.apagon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta para apagones e interrupciones")
public class ApagonResponseDto {
    private Long id;
    private Integer numeroSistema;
    private String descripcion;
    private LocalDateTime fechaReporte;
    private String controladorNombre;
}