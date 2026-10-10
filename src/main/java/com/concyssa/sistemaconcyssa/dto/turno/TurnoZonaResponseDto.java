package com.concyssa.sistemaconcyssa.dto.turno;

import lombok.Data;

@Data
public class TurnoZonaResponseDto {
    private Long id;
    private String mes;
    private String nombreCompleto;
    private String zona;
    private String turno;
}