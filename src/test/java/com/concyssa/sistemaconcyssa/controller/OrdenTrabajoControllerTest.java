package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.orden.OrdenTrabajoCreateDto;
import com.concyssa.sistemaconcyssa.dto.orden.OrdenTrabajoResponseDto;
import com.concyssa.sistemaconcyssa.enums.EstadoOrden;
import com.concyssa.sistemaconcyssa.enums.PrioridadOrden;
import com.concyssa.sistemaconcyssa.service.OrdenTrabajoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdenTrabajoController.class)
public class OrdenTrabajoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrdenTrabajoService ordenTrabajoService;

    @Test
    @DisplayName("GET /api/ordenes - Listar todas las órdenes con rol SUPERVISOR")
    @WithMockUser(roles = {"SUPERVISOR"})
    void deberiaListarTodasLasOrdenes() throws Exception {
        OrdenTrabajoResponseDto dto = new OrdenTrabajoResponseDto(
                1L,
                "OT-2026-001",
                "Mantenimiento preventivo de bomba hidraulica",
                "Av. Javier Prado Este 2465, San Borja",
                EstadoOrden.PENDIENTE,
                PrioridadOrden.ALTA,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                "admin",
                "controlador1"
        );

        List<OrdenTrabajoResponseDto> lista = List.of(dto);

        given(ordenTrabajoService.listarTodas()).willReturn(lista);

        mockMvc.perform(get("/api/ordenes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].codigo").value("OT-2026-001"));
    }

    @Test
    @DisplayName("GET /api/ordenes/{id} - Obtener orden por ID exitosamente")
    @WithMockUser(roles = {"CONTROLADOR"})
    void deberiaObtenerOrdenPorIdExitosamente() throws Exception {
        Long ordenId = 1L;
        OrdenTrabajoResponseDto dto = new OrdenTrabajoResponseDto(
                ordenId,
                "OT-2026-001",
                "Mantenimiento preventivo de bomba hidraulica",
                "Av. Javier Prado Este 2465, San Borja",
                EstadoOrden.PENDIENTE,
                PrioridadOrden.ALTA,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                "admin",
                "controlador1"
        );

        given(ordenTrabajoService.obtenerPorId(ordenId)).willReturn(dto);

        mockMvc.perform(get("/api/ordenes/{id}", ordenId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ordenId))
                .andExpect(jsonPath("$.codigo").value("OT-2026-001"));
    }

    @Test
    @DisplayName("POST /api/ordenes - Crear orden de trabajo con rol ADMIN")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deberiaCrearOrdenTrabajoExitosamente() throws Exception {
        OrdenTrabajoCreateDto createDto = new OrdenTrabajoCreateDto();
        createDto.setDescripcion("Nueva Inspección");
        createDto.setDireccion("Av. Arequipa 123");
        createDto.setPrioridad(PrioridadOrden.ALTA);

        OrdenTrabajoResponseDto dto = new OrdenTrabajoResponseDto(
                1L,
                "OT-2026-002",
                "Nueva Inspección",
                "Av. Arequipa 123",
                EstadoOrden.PENDIENTE,
                PrioridadOrden.ALTA,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "admin",
                "controlador1"
        );

        given(ordenTrabajoService.crearOrden(any(OrdenTrabajoCreateDto.class), anyString())).willReturn(dto);

        mockMvc.perform(post("/api/ordenes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("OT-2026-002"))
                .andExpect(jsonPath("$.descripcion").value("Nueva Inspección"));
    }

    @Test
    @DisplayName("PUT /api/ordenes/{id} - Actualizar orden de trabajo con rol ADMIN")
    @WithMockUser(roles = {"ADMIN"})
    void deberiaActualizarOrdenTrabajoExitosamente() throws Exception {
        Long ordenId = 1L;
        OrdenTrabajoCreateDto updateDto = new OrdenTrabajoCreateDto();
        updateDto.setDescripcion("Inspección Actualizada");

        OrdenTrabajoResponseDto dto = new OrdenTrabajoResponseDto(
                ordenId,
                "OT-2026-001",
                "Inspección Actualizada",
                "Av. Javier Prado Este 2465, San Borja",
                EstadoOrden.EN_PROCESO,
                PrioridadOrden.MEDIA,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "admin",
                "controlador1"
        );

        given(ordenTrabajoService.actualizarOrden(eq(ordenId), any(OrdenTrabajoCreateDto.class))).willReturn(dto);

        mockMvc.perform(put("/api/ordenes/{id}", ordenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Inspección Actualizada"));
    }

    @Test
    @DisplayName("PATCH /api/ordenes/{id}/estado - Cambiar estado de orden exitosamente")
    @WithMockUser(roles = {"CONTROLADOR"})
    void deberiaCambiarEstadoOrdenExitosamente() throws Exception {
        Long ordenId = 1L;
        OrdenTrabajoResponseDto dto = new OrdenTrabajoResponseDto(
                ordenId,
                "OT-2026-001",
                "Mantenimiento preventivo de bomba hidraulica",
                "Av. Javier Prado Este 2465, San Borja",
                EstadoOrden.valueOf("COMPLETADO"),
                PrioridadOrden.ALTA,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "admin",
                "controlador1"
        );

        given(ordenTrabajoService.actualizarEstado(eq(ordenId), any(EstadoOrden.class), anyString(), anyString()))
                .willReturn(dto);

        mockMvc.perform(patch("/api/ordenes/{id}/estado", ordenId)
                        .param("nuevoEstado", "COMPLETADO"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/ordenes/{id} - Eliminar orden con rol ADMIN")
    @WithMockUser(roles = {"ADMIN"})
    void deberiaEliminarOrdenExitosamente() throws Exception {
        Long ordenId = 1L;
        doNothing().when(ordenTrabajoService).eliminarOrden(ordenId);

        mockMvc.perform(delete("/api/ordenes/{id}", ordenId))
                .andExpect(status().isNoContent());
    }
}