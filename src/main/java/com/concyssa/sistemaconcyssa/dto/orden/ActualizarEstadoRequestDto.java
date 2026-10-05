package com.concyssa.sistemaconcyssa.dto.orden;

import com.concyssa.sistemaconcyssa.enums.EstadoOrden;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarEstadoRequestDto {

    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoOrden nuevoEstado;

    private String observacion;
}
