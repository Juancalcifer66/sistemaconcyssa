package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioResponseDto;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import com.concyssa.sistemaconcyssa.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setUsername("testuser");
        usuarioMock.setEmail("test@concyssa.com");
    }

    @Test
    void buscarPorId_Exitoso() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        // When - CAMBIO AQUÍ: Usamos UsuarioResponseDto en lugar de Usuario
        UsuarioResponseDto resultado = usuarioService.buscarPorId(1L);

        // Then
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_NoEncontrado_DebeLanzarExcepcion() {
        // Given
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarPorId(99L);
        });

        assertNotNull(exception);
        verify(usuarioRepository, times(1)).findById(99L);
    }
}