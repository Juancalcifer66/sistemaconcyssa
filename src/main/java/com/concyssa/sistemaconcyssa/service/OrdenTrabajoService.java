package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.orden.HistorialOrdenResponseDto;
import com.concyssa.sistemaconcyssa.dto.orden.OrdenTrabajoCreateDto;
import com.concyssa.sistemaconcyssa.dto.orden.OrdenTrabajoResponseDto;
import com.concyssa.sistemaconcyssa.enums.EstadoOrden;
import com.concyssa.sistemaconcyssa.enums.PrioridadOrden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdenTrabajoService {

    OrdenTrabajoResponseDto crearOrden(OrdenTrabajoCreateDto dto, String usernameCreador);
    
    Page<OrdenTrabajoResponseDto> listarConFiltros(EstadoOrden estado, PrioridadOrden prioridad, Long operadorId, Pageable pageable);

    List<OrdenTrabajoResponseDto> listarTodas();

    OrdenTrabajoResponseDto obtenerPorId(Long id);

    // Firma actualizada para incluir observación y el usuario que realiza la acción
    OrdenTrabajoResponseDto actualizarEstado(Long id, EstadoOrden nuevoEstado, String observacion, String usernameAccion);

    // Nuevo método para consultar la traza de auditoría
    List<HistorialOrdenResponseDto> obtenerHistorialPorOrden(Long ordenId);
}