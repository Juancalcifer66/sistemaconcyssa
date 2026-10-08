package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.error.ErrorResponseDto;
import com.concyssa.sistemaconcyssa.dto.usuario.JwtResponseDto;
import com.concyssa.sistemaconcyssa.dto.usuario.LoginRequestDto;
import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioCreateDto;
import com.concyssa.sistemaconcyssa.entity.Rol;
import com.concyssa.sistemaconcyssa.entity.Usuario;
import com.concyssa.sistemaconcyssa.enums.RolNombre;
import com.concyssa.sistemaconcyssa.repository.RolRepository;
import com.concyssa.sistemaconcyssa.repository.UsuarioRepository;
import com.concyssa.sistemaconcyssa.security.JwtProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Tag(name = "Autenticación", description = "Endpoints para el registro, login y generación de tokens JWT basados en DNI")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Operation(
        summary = "Registrar un usuario", 
        description = "Permite registrar un usuario en el sistema usando su DNI y asignando los roles especificados."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Usuario registrado exitosamente"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "El DNI o email ya existe, o datos de solicitud inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
        )
    })
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@Valid @RequestBody UsuarioCreateDto dto) {
        if (usuarioRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado.");
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty() && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setDni(dto.getDni());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setNombreCompleto(dto.getNombreCompleto() != null ? dto.getNombreCompleto() : "Usuario del Sistema");
        usuario.setEstado(true);

        Set<Rol> roles = new HashSet<>();

        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            Rol defaultRole = rolRepository.findByNombre(RolNombre.ROLE_CONTROLADOR)
                    .orElseThrow(() -> new RuntimeException("Error: El rol ROLE_CONTROLADOR no existe en la base de datos."));
            roles.add(defaultRole);
        } else {
            for (String rolStr : dto.getRoles()) {
                String temporal = rolStr.trim().toUpperCase();

                if (!temporal.startsWith("ROLE_")) {
                    temporal = "ROLE_" + temporal;
                }

                final String nombreFormateado = temporal;

                try {
                    RolNombre rolNombre = RolNombre.valueOf(nombreFormateado);
                    Rol rol = rolRepository.findByNombre(rolNombre)
                            .orElseThrow(() -> new RuntimeException("Error: El rol " + nombreFormateado + " no existe en la base de datos."));
                    roles.add(rol);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("El rol especificado no es válido: " + rolStr);
                }
            }
        }

        usuario.setRoles(roles);
        usuarioRepository.save(usuario);

        return new ResponseEntity<>("Usuario registrado exitosamente.", HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario usando su DNI y contraseña, retornando el token JWT")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Autenticación exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = JwtResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Formato de credenciales inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciales inválidas (DNI o contraseña incorrectos)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> loginUsuario(@Valid @RequestBody LoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getDni(), dto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generarToken(authentication);
        Optional<Usuario> usuarioOpt = usuarioRepository.findByDni(dto.getDni());

        String email = (usuarioOpt.isPresent() && usuarioOpt.get().getEmail() != null) ? usuarioOpt.get().getEmail() : "";
        JwtResponseDto respuesta = new JwtResponseDto(token, dto.getDni(), email);

        return ResponseEntity.ok(respuesta);
    }
}