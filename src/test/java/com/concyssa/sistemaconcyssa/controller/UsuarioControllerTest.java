package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioCreateDto;
import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioResponseDto;
import com.concyssa.sistemaconcyssa.exception.ResourceNotFoundException;
import com.concyssa.sistemaconcyssa.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    @DisplayName("GET /api/usuarios/me - Éxito al obtener perfil actual")
    @WithMockUser(username = "45678912", roles = {"CONTROLADOR"})
    void deberiaObtenerPerfilActualExitosamente() throws Exception {
        String dni = "45678912";
        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                1L, dni, "controlador@concyssa.com", "Juan Pérez", true, Set.of("ROLE_CONTROLADOR")
        );

        given(usuarioService.obtenerPorDni(dni)).willReturn(responseDto);

        mockMvc.perform(get("/api/usuarios/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value(dni))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Pérez"));
    }

    @Test
    @DisplayName("GET /api/usuarios - Listar todos con rol ADMIN")
    @WithMockUser(roles = {"ADMIN"})
    void deberiaListarTodosLosUsuarios() throws Exception {
        List<UsuarioResponseDto> lista = List.of(
                new UsuarioResponseDto(1L, "12345678", "admin@concyssa.com", "Admin User", true, Set.of("ROLE_ADMIN"))
        );

        given(usuarioService.listarTodos()).willReturn(lista);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)));
    }

    @Test
    @DisplayName("POST /api/usuarios - Crear usuario exitosamente con rol ADMIN")
    @WithMockUser(roles = {"ADMIN"})
    void deberiaCrearUsuarioExitosamente() throws Exception {
        UsuarioCreateDto createDto = new UsuarioCreateDto();
        createDto.setDni("12345678");
        createDto.setEmail("nuevo@concyssa.com");
        createDto.setPassword("Password123*");
        createDto.setNombreCompleto("Nuevo Usuario");
        createDto.setRoles(Set.of("ROLE_CONTROLADOR"));

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                1L, "12345678", "nuevo@concyssa.com", "Nuevo Usuario", true, Set.of("ROLE_CONTROLADOR")
        );

        given(usuarioService.crearUsuario(any(UsuarioCreateDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.nombreCompleto").value("Nuevo Usuario"));
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} - Actualizar usuario con rol ADMIN")
    @WithMockUser(roles = {"ADMIN"})
    void deberiaActualizarUsuarioExitosamente() throws Exception {
        Long usuarioId = 1L;
        UsuarioCreateDto updateDto = new UsuarioCreateDto();
        updateDto.setDni("12345678");
        updateDto.setEmail("actualizado@concyssa.com");
        updateDto.setNombreCompleto("Usuario Actualizado");
        updateDto.setRoles(Set.of("ROLE_CONTROLADOR"));

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                usuarioId, "12345678", "actualizado@concyssa.com", "Usuario Actualizado", true, Set.of("ROLE_CONTROLADOR")
        );

        given(usuarioService.actualizarUsuario(eq(usuarioId), any(UsuarioCreateDto.class))).willReturn(responseDto);

        mockMvc.perform(put("/api/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto").value("Usuario Actualizado"));
    }

    @Test
    @DisplayName("PATCH /api/usuarios/{id}/estado - Cambiar estado con rol ADMIN")
    @WithMockUser(roles = {"ADMIN"})
    void deberiaCambiarEstadoUsuarioExitosamente() throws Exception {
        Long usuarioId = 1L;
        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                usuarioId, "12345678", "user@concyssa.com", "User Test", false, Set.of("ROLE_CONTROLADOR")
        );

        given(usuarioService.cambiarEstado(usuarioId)).willReturn(responseDto);

        mockMvc.perform(patch("/api/usuarios/{id}/estado", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(false));
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} - Eliminar usuario con rol ADMIN")
    @WithMockUser(roles = {"ADMIN"})
    void deberiaEliminarUsuarioExitosamente() throws Exception {
        Long usuarioId = 1L;
        doNothing().when(usuarioService).eliminarUsuario(usuarioId);

        mockMvc.perform(delete("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/usuarios/me - Retornar 401 cuando no esta autenticado")
    void deberiaRetornarNoAutorizadoCuandoNoEstaAutenticado() throws Exception {
        mockMvc.perform(get("/api/usuarios/me"))
                .andExpect(status().isUnauthorized());
    }
}