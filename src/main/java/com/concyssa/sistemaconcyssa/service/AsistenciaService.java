package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.asistencia.AsistenciaCreateDto;
import com.concyssa.sistemaconcyssa.dto.asistencia.AsistenciaResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AsistenciaService {
    AsistenciaResponseDto registrarIngreso(AsistenciaCreateDto dto, MultipartFile foto, String dniControlador);
    AsistenciaResponseDto registrarSalida(String dniControlador);
    List<AsistenciaResponseDto> listarPorControlador(Long controladorId);
    List<AsistenciaResponseDto> listarTodas();
}