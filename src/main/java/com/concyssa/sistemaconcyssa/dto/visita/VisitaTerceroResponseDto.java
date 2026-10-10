package com.concyssa.sistemaconcyssa.dto.visita;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta para visitas de terceros")
public class VisitaTerceroResponseDto {
    private Long id;
    private Integer numeroSistema;
    private String estacion;
    private String nombreEmpresa;
    private String motivo;
    private String observaciones;
    private String fotoUrl;
    private LocalDateTime fechaVisita;
    private String controladorNombre;
}