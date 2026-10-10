package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.turno.TurnoZonaCreateDto;
import com.concyssa.sistemaconcyssa.dto.turno.TurnoZonaResponseDto;

import java.util.List;

public interface TurnoZonaService {
    List<TurnoZonaResponseDto> listarPorMes(String mes);
    TurnoZonaResponseDto guardarProgramacion(TurnoZonaCreateDto dto);
    TurnoZonaResponseDto actualizarProgramacion(Long id, TurnoZonaCreateDto dto);
}