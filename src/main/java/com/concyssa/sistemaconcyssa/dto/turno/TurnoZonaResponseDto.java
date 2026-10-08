package com.concyssa.sistemaconcyssa.dto.turno;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta del cronograma de turnos y zonas")
public class TurnoZonaResponseDto {
    private Long id;
    private LocalDate fecha;
    private String turno;
    private String zona;
    private Long controladorId;
    private String controladorNombre;
}