package com.concyssa.sistemaconcyssa.service.impl;

import com.concyssa.sistemaconcyssa.dto.danio.DanioFallaCreateDto;
import com.concyssa.sistemaconcyssa.dto.danio.DanioFallaResponseDto;
import com.concyssa.sistemaconcyssa.entity.DanioFalla;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.exception.ResourceNotFoundException;
import com.concyssa.sistemaconcyssa.repository.DanioFallaRepository;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import com.concyssa.sistemaconcyssa.service.DanioFallaService;
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
public class DanioFallaServiceImpl implements DanioFallaService {

    private final DanioFallaRepository danioFallaRepository;
    private final UsuarioRepository usuarioRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public DanioFallaResponseDto registrarDanio(DanioFallaCreateDto dto, MultipartFile foto, String dniControlador) {
        Usuario controlador = usuarioRepository.findByDni(dniControlador)
                .orElseThrow(() -> new ResourceNotFoundException("Controlador no encontrado con DNI: " + dniControlador));

        String nombreArchivo = null;
        if (foto != null && !foto.isEmpty()) {
            nombreArchivo = fileStorageService.almacenarArchivo(foto);
        }

        DanioFalla danioFalla = new DanioFalla();
        danioFalla.setDescripcion(dto.getDescripcion());
        danioFalla.setFotoUrl(nombreArchivo);
        danioFalla.setFechaReporte(LocalDateTime.now());
        danioFalla.setControlador(controlador);

        DanioFalla guardado = danioFallaRepository.save(danioFalla);
        return mapearADto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DanioFallaResponseDto> listarTodos() {
        return danioFallaRepository.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DanioFallaResponseDto> listarPorControlador(Long controladorId) {
        return danioFallaRepository.findByControladorId(controladorId).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private DanioFallaResponseDto mapearADto(DanioFalla entidad) {
        return new DanioFallaResponseDto(
                entidad.getId(),
                entidad.getDescripcion(),
                entidad.getFotoUrl(),
                entidad.getFechaReporte(),
                entidad.getControlador() != null ? entidad.getControlador().getNombreCompleto() : null
        );
    }
}