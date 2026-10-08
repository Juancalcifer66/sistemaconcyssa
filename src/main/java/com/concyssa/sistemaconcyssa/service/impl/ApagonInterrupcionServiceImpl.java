package com.concyssa.sistemaconcyssa.service.impl;

import com.concyssa.sistemaconcyssa.dto.apagon.ApagonCreateDto;
import com.concyssa.sistemaconcyssa.dto.apagon.ApagonResponseDto;
import com.concyssa.sistemaconcyssa.entity.ApagonInterrupcion;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.exception.ResourceNotFoundException;
import com.concyssa.sistemaconcyssa.repository.ApagonInterrupcionRepository;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import com.concyssa.sistemaconcyssa.service.ApagonInterrupcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApagonInterrupcionServiceImpl implements ApagonInterrupcionService {

    private final ApagonInterrupcionRepository repository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ApagonResponseDto registrarApagon(ApagonCreateDto dto, String dniControlador) {
        Usuario controlador = usuarioRepository.findByDni(dniControlador)
                .orElseThrow(() -> new ResourceNotFoundException("Controlador no encontrado con DNI: " + dniControlador));

        ApagonInterrupcion entidad = new ApagonInterrupcion();
        entidad.setNumeroSistema(dto.getNumeroSistema());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setFechaReporte(LocalDateTime.now());
        entidad.setControlador(controlador);

        ApagonInterrupcion guardado = repository.save(entidad);
        return new ApagonResponseDto(
                guardado.getId(),
                guardado.getNumeroSistema(),
                guardado.getDescripcion(),
                guardado.getFechaReporte(),
                guardado.getControlador().getNombreCompleto()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApagonResponseDto> listarTodos() {
        return repository.findAll().stream().map(e -> new ApagonResponseDto(
                e.getId(), e.getNumeroSistema(), e.getDescripcion(), e.getFechaReporte(), e.getControlador().getNombreCompleto()
        )).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApagonResponseDto> listarPorControlador(Long controladorId) {
        return repository.findByControladorId(controladorId).stream().map(e -> new ApagonResponseDto(
                e.getId(), e.getNumeroSistema(), e.getDescripcion(), e.getFechaReporte(), e.getControlador().getNombreCompleto()
        )).collect(Collectors.toList());
    }
}