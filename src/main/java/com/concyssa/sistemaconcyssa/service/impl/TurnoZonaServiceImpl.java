package com.concyssa.sistemaconcyssa.service.impl;

import com.concyssa.sistemaconcyssa.dto.turno.TurnoZonaCreateDto;
import com.concyssa.sistemaconcyssa.dto.turno.TurnoZonaResponseDto;
import com.concyssa.sistemaconcyssa.entity.TurnoZona;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.exception.ResourceNotFoundException;
import com.concyssa.sistemaconcyssa.repository.TurnoZonaRepository;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import com.concyssa.sistemaconcyssa.service.TurnoZonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurnoZonaServiceImpl implements TurnoZonaService {

    private final TurnoZonaRepository repository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public TurnoZonaResponseDto asignarTurno(TurnoZonaCreateDto dto) {
        Usuario controlador = usuarioRepository.findById(dto.getControladorId())
                .orElseThrow(() -> new ResourceNotFoundException("Controlador no encontrado con ID: " + dto.getControladorId()));

        TurnoZona entidad = new TurnoZona();
        entidad.setFecha(dto.getFecha());
        entidad.setTurno(dto.getTurno());
        entidad.setZona(dto.getZona());
        entidad.setControlador(controlador);

        TurnoZona guardado = repository.save(entidad);
        return mapearADto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoZonaResponseDto> listarPorMes(int anio, int mes) {
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
        return repository.findByRangoFechas(inicio, fin).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoZonaResponseDto> listarTodos() {
        return repository.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private TurnoZonaResponseDto mapearADto(TurnoZona e) {
        return new TurnoZonaResponseDto(
                e.getId(),
                e.getFecha(),
                e.getTurno(),
                e.getZona(),
                e.getControlador().getId(),
                e.getControlador().getNombreCompleto()
        );
    }
}