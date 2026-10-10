package com.concyssa.sistemaconcyssa.service.impl;

import com.concyssa.sistemaconcyssa.dto.turno.TurnoZonaCreateDto;
import com.concyssa.sistemaconcyssa.dto.turno.TurnoZonaResponseDto;
import com.concyssa.sistemaconcyssa.entity.TurnoZona;
import com.concyssa.sistemaconcyssa.repository.TurnoZonaRepository;
import com.concyssa.sistemaconcyssa.service.TurnoZonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurnoZonaServiceImpl implements TurnoZonaService {

    private final TurnoZonaRepository repository;

    @Override
    public List<TurnoZonaResponseDto> listarPorMes(String mes) {
        return repository.findByMesIgnoreCase(mes)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TurnoZonaResponseDto guardarProgramacion(TurnoZonaCreateDto dto) {
        TurnoZona entity = new TurnoZona();
        entity.setMes(dto.getMes());
        entity.setNombreCompleto(dto.getNombreCompleto());
        entity.setZona(dto.getZona());
        entity.setTurno(dto.getTurno());

        TurnoZona guardado = repository.save(entity);
        return mapToResponse(guardado);
    }

    @Override
    public TurnoZonaResponseDto actualizarProgramacion(Long id, TurnoZonaCreateDto dto) {
        TurnoZona entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programación no encontrada con ID: " + id));

        entity.setMes(dto.getMes());
        entity.setNombreCompleto(dto.getNombreCompleto());
        entity.setZona(dto.getZona());
        entity.setTurno(dto.getTurno());

        TurnoZona actualizado = repository.save(entity);
        return mapToResponse(actualizado);
    }

    private TurnoZonaResponseDto mapToResponse(TurnoZona entity) {
        TurnoZonaResponseDto response = new TurnoZonaResponseDto();
        response.setId(entity.getId());
        response.setMes(entity.getMes());
        response.setNombreCompleto(entity.getNombreCompleto());
        response.setZona(entity.getZona());
        response.setTurno(entity.getTurno());
        return response;
    }
}