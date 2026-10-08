package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioCreateDto;
import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioResponseDto;

import java.util.List;

public interface UsuarioService {
    List<UsuarioResponseDto> listarTodos();
    UsuarioResponseDto buscarPorId(Long id);
    UsuarioResponseDto obtenerPorDni(String dni); // Método agregado
    List<UsuarioResponseDto> listarControladores();
    UsuarioResponseDto crearUsuario(UsuarioCreateDto dto);
    UsuarioResponseDto actualizarUsuario(Long id, UsuarioCreateDto dto);
    void eliminarUsuario(Long id);
    UsuarioResponseDto cambiarEstado(Long id);
}