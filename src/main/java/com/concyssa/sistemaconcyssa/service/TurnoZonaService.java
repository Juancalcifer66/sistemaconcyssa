package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.turno.TurnoZonaCreateDto;
import com.concyssa.sistemaconcyssa.dto.turno.TurnoZonaResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface TurnoZonaService {
    TurnoZonaResponseDto asignarTurno(TurnoZonaCreateDto dto);
    List<TurnoZonaResponseDto> listarPorMes(int anio, int mes);
    List<TurnoZonaResponseDto> listarTodos();
}