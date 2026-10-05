package com.concyssa.sistemaconcyssa.service;

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
import com.concyssa.sistemaconcyssa.service.impl.OrdenTrabajoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenTrabajoServiceTest {

    @Mock
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @Mock
    private HistorialOrdenRepository historialOrdenRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private OrdenTrabajoServiceImpl ordenTrabajoService;

    private OrdenTrabajo ordenEjemplo;
    private Usuario usuarioEjemplo;
    private Usuario creadorEjemplo;

    @BeforeEach
    void setUp() {
        // Usuario que realiza las acciones en las pruebas
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId(1L);
        usuarioEjemplo.setUsername("operador1");

        // Usuario creador para evitar NullPointerException en mapToResponseDto
        creadorEjemplo = new Usuario();
        creadorEjemplo.setId(2L);
        creadorEjemplo.setUsername("admin");

        // Orden de prueba base completamente poblada
        ordenEjemplo = crearOrdenMock(10L, "OT-2026-001", EstadoOrden.PENDIENTE, PrioridadOrden.ALTA);
    }

    @Test
    @DisplayName("Debe actualizar el estado de la orden y registrar el evento en el historial")
    void actualizarEstado_Exito() {
        // Arrange
        Long ordenId = 10L;
        EstadoOrden nuevoEstado = EstadoOrden.EN_PROCESO;
        String observacion = "Se inicia la revisión técnica en campo.";
        String usernameAccion = "operador1";

        when(ordenTrabajoRepository.findById(ordenId)).thenReturn(Optional.of(ordenEjemplo));
        when(usuarioRepository.findByUsername(usernameAccion)).thenReturn(Optional.of(usuarioEjemplo));
        when(ordenTrabajoRepository.save(any(OrdenTrabajo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrdenTrabajoResponseDto resultado = ordenTrabajoService.actualizarEstado(
                ordenId, nuevoEstado, observacion, usernameAccion
        );

        // Assert - Respuesta DTO
        assertNotNull(resultado, "El DTO devuelto no debe ser nulo");
        assertEquals(nuevoEstado, resultado.getEstado());
        assertEquals("OT-2026-001", resultado.getCodigo());

        // Assert - Entidad actualizada
        assertEquals(nuevoEstado, ordenEjemplo.getEstado());
        verify(ordenTrabajoRepository, times(1)).save(ordenEjemplo);

        // Assert - Historial guardado
        ArgumentCaptor<HistorialOrden> historialCaptor = ArgumentCaptor.forClass(HistorialOrden.class);
        verify(historialOrdenRepository, times(1)).save(historialCaptor.capture());

        HistorialOrden historialGuardado = historialCaptor.getValue();
        assertNotNull(historialGuardado, "El registro de historial capturado no debe ser nulo");
        assertEquals(EstadoOrden.PENDIENTE, historialGuardado.getEstadoAnterior());
        assertEquals(EstadoOrden.EN_PROCESO, historialGuardado.getEstadoNuevo());
        assertEquals(observacion, historialGuardado.getObservacion());
        assertEquals(usuarioEjemplo, historialGuardado.getUsuario());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando la orden de trabajo no existe")
    void actualizarEstado_OrdenNoEncontrada_LanzaExcepcion() {
        // Arrange
        Long ordenIdInexistente = 99L;
        when(ordenTrabajoRepository.findById(ordenIdInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException excepcion = assertThrows(
                ResourceNotFoundException.class,
                () -> ordenTrabajoService.actualizarEstado(ordenIdInexistente, EstadoOrden.EN_PROCESO, "Observación", "admin")
        );

        assertNotNull(excepcion);
        assertTrue(excepcion.getMessage().contains("99"), "El mensaje debe contener el ID no encontrado");

        // Verificaciones de seguridad (No se debe persistir nada)
        verify(ordenTrabajoRepository, never()).save(any());
        verify(historialOrdenRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el usuario no existe en la BD")
    void actualizarEstado_UsuarioNoEncontrado_LanzaExcepcion() {
        // Arrange
        Long ordenId = 10L;
        String usernameInexistente = "usuario_fantasma";

        when(ordenTrabajoRepository.findById(ordenId)).thenReturn(Optional.of(ordenEjemplo));
        when(usuarioRepository.findByUsername(usernameInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException excepcion = assertThrows(
                ResourceNotFoundException.class,
                () -> ordenTrabajoService.actualizarEstado(ordenId, EstadoOrden.EN_PROCESO, "Observación", usernameInexistente)
        );

        assertNotNull(excepcion);

        // Verificaciones de seguridad
        verify(ordenTrabajoRepository, never()).save(any());
        verify(historialOrdenRepository, never()).save(any());
    }

    // Helper centralizado para construir instancias válidas de OrdenTrabajo
    private OrdenTrabajo crearOrdenMock(Long id, String codigo, EstadoOrden estado, PrioridadOrden prioridad) {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setId(id);
        ot.setCodigo(codigo);
        ot.setDescripcion("Mantenimiento preventivo de estación de bombeo");
        ot.setEstado(estado);
        ot.setPrioridad(prioridad);
        ot.setCreador(creadorEjemplo); // Garantiza que 'creador' no sea null para mapToResponseDto
        ot.setFechaCreacion(LocalDateTime.now());
        return ot;
    }
}