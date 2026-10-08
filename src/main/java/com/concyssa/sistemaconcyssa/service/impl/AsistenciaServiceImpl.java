package com.concyssa.sistemaconcyssa.service.impl;

import com.concyssa.sistemaconcyssa.dto.asistencia.AsistenciaCreateDto;
import com.concyssa.sistemaconcyssa.dto.asistencia.AsistenciaResponseDto;
import com.concyssa.sistemaconcyssa.entity.Asistencia;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.exception.ResourceNotFoundException;
import com.concyssa.sistemaconcyssa.repository.AsistenciaRepository;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import com.concyssa.sistemaconcyssa.service.AsistenciaService;
import com.concyssa.sistemaconcyssa.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public AsistenciaResponseDto registrarIngreso(AsistenciaCreateDto dto, MultipartFile foto, String dniControlador) {
        Usuario controlador = usuarioRepository.findByDni(dniControlador)
                .orElseThrow(() -> new ResourceNotFoundException("Controlador no encontrado con DNI: " + dniControlador));

        // Validar si ya tiene un ingreso abierto sin salida registrada
        asistenciaRepository.findTopByControladorIdAndFechaSalidaIsNullOrderByFechaIngresoDesc(controlador.getId())
                .ifPresent(a -> {
                    throw new IllegalStateException("El controlador ya cuenta con un registro de ingreso activo sin marcar salida.");
                });

        String nombreArchivo = null;
        if (foto != null && !foto.isEmpty()) {
            nombreArchivo = fileStorageService.almacenarArchivo(foto);
        }

        Asistencia asistencia = new Asistencia();
        asistencia.setFechaIngreso(LocalDateTime.now());
        asistencia.setFotoUrl(nombreArchivo);
        asistencia.setObservacion(dto.getObservacion());
        asistencia.setControlador(controlador);

        Asistencia guardada = asistenciaRepository.save(asistencia);
        return mapearADto(guardada);
    }

    @Override
    @Transactional
    public AsistenciaResponseDto registrarSalida(String dniControlador) {
        Usuario controlador = usuarioRepository.findByDni(dniControlador)
                .orElseThrow(() -> new ResourceNotFoundException("Controlador no encontrado con DNI: " + dniControlador));

        Asistencia asistenciaActiva = asistenciaRepository.findTopByControladorIdAndFechaSalidaIsNullOrderByFechaIngresoDesc(controlador.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningún registro de ingreso activo para marcar salida."));

        asistenciaActiva.setFechaSalida(LocalDateTime.now());
        Asistencia actualizada = asistenciaRepository.save(asistenciaActiva);
        
        return mapearADto(actualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaResponseDto> listarPorControlador(Long controladorId) {
        return asistenciaRepository.findByControladorId(controladorId).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaResponseDto> listarTodas() {
        return asistenciaRepository.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private AsistenciaResponseDto mapearADto(Asistencia entidad) {
        return new AsistenciaResponseDto(
                entidad.getId(),
                entidad.getFechaIngreso(),
                entidad.getFechaSalida(),
                entidad.getFotoUrl(),
                entidad.getObservacion(),
                entidad.getControlador() != null ? entidad.getControlador().getNombreCompleto() : null
        );
    }
}