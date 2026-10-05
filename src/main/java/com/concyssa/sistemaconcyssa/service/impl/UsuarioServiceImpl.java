package com.concyssa.sistemaconcyssa.service.impl;

import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioCreateDto;
import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioResponseDto;
import com.concyssa.sistemaconcyssa.entity.Rol;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.enums.RolNombre;
import com.concyssa.sistemaconcyssa.exception.ResourceNotFoundException;
import com.concyssa.sistemaconcyssa.repository.RolRepository;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import com.concyssa.sistemaconcyssa.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponseDto crearUsuario(UsuarioCreateDto dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado");
        }

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEstado(true);

        Set<Rol> roles = new HashSet<>();
        for (String rolStr : dto.getRoles()) {
            RolNombre rolNombre = RolNombre.valueOf(rolStr);
            Rol rol = rolRepository.findByNombre(rolNombre)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + rolStr));
            roles.add(rol);
        }
        usuario.setRoles(roles);

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToResponseDto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return mapToResponseDto(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDto cambiarEstado(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        
        // Alterna el estado actual (si está activo lo desactiva, y viceversa)
        usuario.setEstado(!usuario.isEstado());
        Usuario actualizado = usuarioRepository.save(usuario);
        return mapToResponseDto(actualizado);
    }

    private UsuarioResponseDto mapToResponseDto(Usuario usuario) {
        Set<String> rolesStr = usuario.getRoles().stream()
                .map(rol -> rol.getNombre().name())
                .collect(Collectors.toSet());

        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNombreCompleto(),
                usuario.isEstado(),
                rolesStr
        );
    }
}