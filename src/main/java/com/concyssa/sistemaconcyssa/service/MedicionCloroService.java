package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.cloro.MedicionCloroCreateDto;
import com.concyssa.sistemaconcyssa.dto.cloro.MedicionCloroResponseDto;

import java.util.List;

public interface MedicionCloroService {
    MedicionCloroResponseDto registrarMedicion(MedicionCloroCreateDto dto, String dniControlador);
    List<MedicionCloroResponseDto> listarTodas();
    List<MedicionCloroResponseDto> listarPorControlador(Long controladorId);
}