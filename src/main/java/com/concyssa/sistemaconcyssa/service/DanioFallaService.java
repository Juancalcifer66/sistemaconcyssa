package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.danio.DanioFallaCreateDto;
import com.concyssa.sistemaconcyssa.dto.danio.DanioFallaResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DanioFallaService {
    DanioFallaResponseDto registrarDanio(DanioFallaCreateDto dto, MultipartFile foto, String dniControlador);
    List<DanioFallaResponseDto> listarTodos();
    List<DanioFallaResponseDto> listarPorControlador(Long controladorId);
}