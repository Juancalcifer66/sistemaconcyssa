package com.concyssa.sistemaconcyssa.service;

import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioCreateDto;
import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioResponseDto;

import java.util.List;

public interface UsuarioService {
    List<UsuarioResponseDto> listarTodos();
    UsuarioResponseDto buscarPorId(Long id);
    UsuarioResponseDto crearUsuario(UsuarioCreateDto dto);
    UsuarioResponseDto cambiarEstado(Long id);
}