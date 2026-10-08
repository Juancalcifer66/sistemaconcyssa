package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.visita.VisitaTerceroCreateDto;
import com.concyssa.sistemaconcyssa.dto.visita.VisitaTerceroResponseDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface VisitaTerceroService {
    VisitaTerceroResponseDto registrarVisita(VisitaTerceroCreateDto dto, MultipartFile foto, String dniControlador);
    List<VisitaTerceroResponseDto> listarTodas();
    List<VisitaTerceroResponseDto> listarPorControlador(Long controladorId);
}