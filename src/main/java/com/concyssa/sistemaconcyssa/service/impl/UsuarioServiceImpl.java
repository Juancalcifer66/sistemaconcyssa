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
        if (usuarioRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty() && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setDni(dto.getDni());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEstado(true);

        Set<Rol> roles = new HashSet<>();
        if (dto.getRoles() != null) {
            for (String rolStr : dto.getRoles()) {
                RolNombre rolNombre = RolNombre.valueOf(rolStr);
                Rol rol = rolRepository.findByNombre(rolNombre)
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + rolStr));
                roles.add(rol);
            }
        }
        usuario.setRoles(roles);

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToResponseDto(guardado);
    }

    @Override
    @Transactional
    public UsuarioResponseDto actualizarUsuario(Long id, UsuarioCreateDto dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Validar si el nuevo DNI ya pertenece a otro usuario diferente
        if (!usuario.getDni().equals(dto.getDni()) && usuarioRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        // Validar si el nuevo email ya pertenece a otro usuario diferente
        if (dto.getEmail() != null && !dto.getEmail().isEmpty() && !dto.getEmail().equals(usuario.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        usuario.setDni(dto.getDni());
        usuario.setEmail(dto.getEmail());
        usuario.setNombreCompleto(dto.getNombreCompleto());

        // Si se proporciona una contraseña nueva, la encriptamos y actualizamos
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Actualizar roles
        Set<Rol> roles = new HashSet<>();
        if (dto.getRoles() != null) {
            for (String rolStr : dto.getRoles()) {
                RolNombre rolNombre = RolNombre.valueOf(rolStr);
                Rol rol = rolRepository.findByNombre(rolNombre)
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + rolStr));
                roles.add(rol);
            }
        }
        usuario.setRoles(roles);

        Usuario actualizado = usuarioRepository.save(usuario);
        return mapToResponseDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
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
    public List<UsuarioResponseDto> listarControladores() {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getRoles().stream()
                        .anyMatch(rol -> rol.getNombre() == RolNombre.ROLE_CONTROLADOR))
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
    @Transactional(readOnly = true)
    public UsuarioResponseDto obtenerPorDni(String dni) {
        Usuario usuario = usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con DNI: " + dni));
        return mapToResponseDto(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDto cambiarEstado(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        
        // Alterna el estado actual (activo/inactivo)
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
                usuario.getDni(),
                usuario.getEmail(),
                usuario.getNombreCompleto(),
                usuario.isEstado(),
                rolesStr
        );
    }
}