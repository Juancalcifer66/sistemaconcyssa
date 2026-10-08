package com.concyssa.sistemaconcyssa.service.impl;

import com.concyssa.sistemaconcyssa.dto.cloro.MedicionCloroCreateDto;
import com.concyssa.sistemaconcyssa.dto.cloro.MedicionCloroResponseDto;
import com.concyssa.sistemaconcyssa.entity.MedicionCloro;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.exception.ResourceNotFoundException;
import com.concyssa.sistemaconcyssa.repository.MedicionCloroRepository;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import com.concyssa.sistemaconcyssa.service.MedicionCloroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicionCloroServiceImpl implements MedicionCloroService {

    private final MedicionCloroRepository medicionCloroRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public MedicionCloroResponseDto registrarMedicion(MedicionCloroCreateDto dto, String dniControlador) {
        Usuario controlador = usuarioRepository.findByDni(dniControlador)
                .orElseThrow(() -> new ResourceNotFoundException("Controlador no encontrado con DNI: " + dniControlador));

        MedicionCloro medicion = new MedicionCloro();
        medicion.setValorCloroMgL(dto.getValorCloroMgL());
        medicion.setUbicacionPunto(dto.getUbicacionPunto());
        medicion.setObservacion(dto.getObservacion());
        medicion.setFechaMedicion(LocalDateTime.now());
        medicion.setControlador(controlador);

        MedicionCloro guardada = medicionCloroRepository.save(medicion);
        return mapearADto(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicionCloroResponseDto> listarTodas() {
        return medicionCloroRepository.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicionCloroResponseDto> listarPorControlador(Long controladorId) {
        return medicionCloroRepository.findByControladorId(controladorId).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private MedicionCloroResponseDto mapearADto(MedicionCloro entidad) {
        return new MedicionCloroResponseDto(
                entidad.getId(),
                entidad.getValorCloroMgL(),
                entidad.getUbicacionPunto(),
                entidad.getObservacion(),
                entidad.getFechaMedicion(),
                entidad.getControlador() != null ? entidad.getControlador().getNombreCompleto() : null
        );
    }
}