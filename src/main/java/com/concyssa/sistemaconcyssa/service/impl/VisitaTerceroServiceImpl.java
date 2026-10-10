package com.concyssa.sistemaconcyssa.service.impl;

import com.concyssa.sistemaconcyssa.dto.visita.VisitaTerceroCreateDto;
import com.concyssa.sistemaconcyssa.dto.visita.VisitaTerceroResponseDto;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.entity.VisitaTercero;
import com.concyssa.sistemaconcyssa.exception.ResourceNotFoundException;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import com.concyssa.sistemaconcyssa.repository.VisitaTerceroRepository;
import com.concyssa.sistemaconcyssa.service.FileStorageService;
import com.concyssa.sistemaconcyssa.service.VisitaTerceroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitaTerceroServiceImpl implements VisitaTerceroService {

    private final VisitaTerceroRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public VisitaTerceroResponseDto registrarVisita(VisitaTerceroCreateDto dto, MultipartFile foto, String dniControlador) {
        Usuario controlador = usuarioRepository.findByDni(dniControlador)
                .orElseThrow(() -> new ResourceNotFoundException("Controlador no encontrado con DNI: " + dniControlador));

        String nombreArchivo = null;
        if (foto != null && !foto.isEmpty()) {
            nombreArchivo = fileStorageService.almacenarArchivo(foto);
        }

        VisitaTercero entidad = new VisitaTercero();
        entidad.setNumeroSistema(dto.getNumeroSistema());
        entidad.setEstacion(dto.getEstacion());
        entidad.setNombreEmpresa(dto.getNombreEmpresa());
        entidad.setMotivo(dto.getMotivo());
        entidad.setObservaciones(dto.getObservaciones());
        entidad.setFotoUrl(nombreArchivo);
        entidad.setFechaVisita(LocalDateTime.now());
        entidad.setControlador(controlador);

        VisitaTercero guardado = repository.save(entidad);
        return mapearADto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VisitaTerceroResponseDto> listarTodas() {
        return repository.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VisitaTerceroResponseDto> listarPorControlador(Long controladorId) {
        return repository.findByControladorId(controladorId).stream().map(this::mapearADto).collect(Collectors.toList());
    }

    private VisitaTerceroResponseDto mapearADto(VisitaTercero e) {
        return new VisitaTerceroResponseDto(
                e.getId(),
                e.getNumeroSistema(),
                e.getEstacion(),
                e.getNombreEmpresa(),
                e.getMotivo(),
                e.getObservaciones(),
                e.getFotoUrl(),
                e.getFechaVisita(),
                e.getControlador().getNombreCompleto()
        );
    }
}