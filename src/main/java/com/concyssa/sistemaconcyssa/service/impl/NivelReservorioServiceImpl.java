package com.concyssa.sistemaconcyssa.service.impl;

import com.concyssa.sistemaconcyssa.dto.reservorio.NivelReservorioCreateDto;
import com.concyssa.sistemaconcyssa.dto.reservorio.NivelReservorioResponseDto;
import com.concyssa.sistemaconcyssa.entity.NivelReservorio;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.exception.ResourceNotFoundException;
import com.concyssa.sistemaconcyssa.repository.NivelReservorioRepository;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import com.concyssa.sistemaconcyssa.service.NivelReservorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NivelReservorioServiceImpl implements NivelReservorioService {

    private final NivelReservorioRepository nivelReservorioRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public NivelReservorioResponseDto registrarNivel(NivelReservorioCreateDto dto, String dniControlador) {
        Usuario controlador = usuarioRepository.findByDni(dniControlador)
                .orElseThrow(() -> new ResourceNotFoundException("Controlador no encontrado con DNI: " + dniControlador));

        NivelReservorio nivel = new NivelReservorio();
        nivel.setNumeroSistema(dto.getNumeroSistema());
        nivel.setEstacion(dto.getEstacion());
        nivel.setPasosLlenos(dto.getPasosLlenos());
        nivel.setPasosLibres(dto.getPasosLibres());
        nivel.setObservacion(dto.getObservacion());
        nivel.setFechaRegistro(LocalDateTime.now());
        nivel.setControlador(controlador);

        NivelReservorio guardado = nivelReservorioRepository.save(nivel);
        return mapearADto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NivelReservorioResponseDto> listarTodos() {
        return nivelReservorioRepository.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NivelReservorioResponseDto> listarPorControlador(Long controladorId) {
        return nivelReservorioRepository.findByControladorId(controladorId).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private NivelReservorioResponseDto mapearADto(NivelReservorio entidad) {
        return new NivelReservorioResponseDto(
                entidad.getId(),
                entidad.getNumeroSistema(),
                entidad.getEstacion(),
                entidad.getPasosLlenos(),
                entidad.getPasosLibres(),
                entidad.getObservacion(),
                entidad.getFechaRegistro(),
                entidad.getControlador() != null ? entidad.getControlador().getNombreCompleto() : null
        );
    }
}