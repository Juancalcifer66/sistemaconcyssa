package com.concyssa.sistemaconcyssa.dto.visita;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para registrar una visita de terceros en campo")
public class VisitaTerceroCreateDto {

    @Schema(description = "Número de sistema asociado", example = "181")
    @NotNull(message = "El número de sistema es obligatorio")
    private Integer numeroSistema;

    @Schema(description = "Estación o ubicación visitada", example = "Rp-01")
    private String estacion;

    @Schema(description = "Nombre de la empresa externa o contratista", example = "Contratistas Generales S.A.C.")
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombreEmpresa;

    @Schema(description = "Motivo u objetivo de la visita", example = "Verificación de niveles y estado de compuertas")
    private String motivo;

    @Schema(description = "Observaciones de la visita", example = "Operación estable sin anomalías detectadas.")
    private String observaciones;
}