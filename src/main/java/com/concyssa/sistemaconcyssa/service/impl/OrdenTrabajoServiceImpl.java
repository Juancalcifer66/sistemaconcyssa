package com.concyssa.sistemaconcyssa.service.impl;

import com.concyssa.sistemaconcyssa.dto.orden.HistorialOrdenResponseDto;
import com.concyssa.sistemaconcyssa.dto.orden.OrdenTrabajoCreateDto;
import com.concyssa.sistemaconcyssa.dto.orden.OrdenTrabajoResponseDto;
import com.concyssa.sistemaconcyssa.entity.HistorialOrden;
import com.concyssa.sistemaconcyssa.entity.OrdenTrabajo;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.enums.EstadoOrden;
import com.concyssa.sistemaconcyssa.enums.PrioridadOrden;
import com.concyssa.sistemaconcyssa.exception.ResourceNotFoundException;
import com.concyssa.sistemaconcyssa.repository.HistorialOrdenRepository;
import com.concyssa.sistemaconcyssa.repository.OrdenTrabajoRepository;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import com.concyssa.sistemaconcyssa.repository.specification.OrdenTrabajoSpecification;
import com.concyssa.sistemaconcyssa.service.OrdenTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenTrabajoServiceImpl implements OrdenTrabajoService {

    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HistorialOrdenRepository historialOrdenRepository;

    @Override
    @Transactional
    public OrdenTrabajoResponseDto crearOrden(OrdenTrabajoCreateDto dto, String dniCreador) {
        if (ordenTrabajoRepository.existsByCodigo(dto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una orden de trabajo con el codigo: " + dto.getCodigo());
        }

        Usuario creador = usuarioRepository.findByDni(dniCreador)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado con DNI: " + dniCreador));

        OrdenTrabajo orden = new OrdenTrabajo();
        orden.setCodigo(dto.getCodigo());
        orden.setDescripcion(dto.getDescripcion());
        orden.setDireccion(dto.getDireccion());
        orden.setPrioridad(dto.getPrioridad());
        orden.setFechaProgramada(dto.getFechaProgramada());
        orden.setCreador(creador);

        if (dto.getOperadorAsignadoId() != null) {
            Usuario operador = usuarioRepository.findById(dto.getOperadorAsignadoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Operador no encontrado con ID: " + dto.getOperadorAsignadoId()));
            orden.setOperadorAsignado(operador);
        }

        OrdenTrabajo guardada = ordenTrabajoRepository.save(orden);
        return mapToResponseDto(guardada);
    }

    @Override
    @Transactional
    public OrdenTrabajoResponseDto actualizarOrden(Long id, OrdenTrabajoCreateDto dto) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de trabajo no encontrada con ID: " + id));

        // Validar si el código cambió y ya pertenece a otra orden
        if (!orden.getCodigo().equals(dto.getCodigo()) && ordenTrabajoRepository.existsByCodigo(dto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe otra orden de trabajo con el código: " + dto.getCodigo());
        }

        orden.setCodigo(dto.getCodigo());
        orden.setDescripcion(dto.getDescripcion());
        orden.setDireccion(dto.getDireccion());
        orden.setPrioridad(dto.getPrioridad());
        orden.setFechaProgramada(dto.getFechaProgramada());

        if (dto.getOperadorAsignadoId() != null) {
            Usuario operador = usuarioRepository.findById(dto.getOperadorAsignadoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Operador no encontrado con ID: " + dto.getOperadorAsignadoId()));
            orden.setOperadorAsignado(operador);
        } else {
            orden.setOperadorAsignado(null);
        }

        OrdenTrabajo actualizada = ordenTrabajoRepository.save(orden);
        return mapToResponseDto(actualizada);
    }

    @Override
    @Transactional
    public void eliminarOrden(Long id) {
        if (!ordenTrabajoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Orden de trabajo no encontrada con ID: " + id);
        }
        ordenTrabajoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajoResponseDto> listarTodas() {
        return ordenTrabajoRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenTrabajoResponseDto obtenerPorId(Long id) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de trabajo no encontrada con ID: " + id));
        return mapToResponseDto(orden);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OrdenTrabajoResponseDto> listarConFiltros(EstadoOrden estado, PrioridadOrden prioridad, Long operadorId, Pageable pageable) {
        Specification<OrdenTrabajo> spec = OrdenTrabajoSpecification.conFiltros(estado, prioridad, operadorId);
        return ordenTrabajoRepository.findAll(spec, pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    @Transactional
    public OrdenTrabajoResponseDto actualizarEstado(Long id, EstadoOrden nuevoEstado, String observacion, String dniAccion) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de trabajo no encontrada con ID: " + id));

        Usuario usuarioAccion = usuarioRepository.findByDni(dniAccion)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con DNI: " + dniAccion));

        EstadoOrden estadoAnterior = orden.getEstado();

        // 1. Actualizar estado en la entidad principal
        orden.setEstado(nuevoEstado);
        OrdenTrabajo actualizada = ordenTrabajoRepository.save(orden);

        // 2. Guardar registro de auditoría en el historial
        HistorialOrden historial = new HistorialOrden();
        historial.setOrdenTrabajo(actualizada);
        historial.setUsuario(usuarioAccion);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(nuevoEstado);
        historial.setObservacion(observacion);

        historialOrdenRepository.save(historial);

        return mapToResponseDto(actualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialOrdenResponseDto> obtenerHistorialPorOrden(Long ordenId) {
        if (!ordenTrabajoRepository.existsById(ordenId)) {
            throw new ResourceNotFoundException("Orden de trabajo no encontrada con ID: " + ordenId);
        }

        return historialOrdenRepository.findByOrdenTrabajoIdOrderByFechaCambioDesc(ordenId)
                .stream()
                .map(h -> new HistorialOrdenResponseDto(
                        h.getId(),
                        h.getOrdenTrabajo().getId(),
                        h.getUsuario().getDni(),
                        h.getEstadoAnterior(),
                        h.getEstadoNuevo(),
                        h.getObservacion(),
                        h.getFechaCambio()
                ))
                .collect(Collectors.toList());
    }

    private OrdenTrabajoResponseDto mapToResponseDto(OrdenTrabajo orden) {
        String operadorDni = orden.getOperadorAsignado() != null ? orden.getOperadorAsignado().getDni() : null;

        return new OrdenTrabajoResponseDto(
                orden.getId(),
                orden.getCodigo(),
                orden.getDescripcion(),
                orden.getDireccion(),
                orden.getEstado(),
                orden.getPrioridad(),
                orden.getFechaCreacion(),
                orden.getFechaProgramada(),
                orden.getCreador().getDni(),
                operadorDni
        );
    }
}