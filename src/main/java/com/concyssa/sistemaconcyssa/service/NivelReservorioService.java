package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.reservorio.NivelReservorioCreateDto;
import com.concyssa.sistemaconcyssa.dto.reservorio.NivelReservorioResponseDto;

import java.util.List;

public interface NivelReservorioService {
    NivelReservorioResponseDto registrarNivel(NivelReservorioCreateDto dto, String dniControlador);
    List<NivelReservorioResponseDto> listarTodos();
    List<NivelReservorioResponseDto> listarPorControlador(Long controladorId);
}