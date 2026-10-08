package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.apagon.ApagonCreateDto;
import com.concyssa.sistemaconcyssa.dto.apagon.ApagonResponseDto;
import java.util.List;

public interface ApagonInterrupcionService {
    ApagonResponseDto registrarApagon(ApagonCreateDto dto, String dniControlador);
    List<ApagonResponseDto> listarTodos();
    List<ApagonResponseDto> listarPorControlador(Long controladorId);
}