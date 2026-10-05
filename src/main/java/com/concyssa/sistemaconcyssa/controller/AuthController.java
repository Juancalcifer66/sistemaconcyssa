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

@Tag(name = "Autenticación", description = "Endpoints para el registro, login y generación de tokens JWT")
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
        description = "Permite registrar un usuario en el sistema asignando los roles especificados en la solicitud."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Usuario registrado exitosamente"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "El nombre de usuario o email ya existe, o datos de solicitud inválidos",
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
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe.");
        }

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setNombreCompleto(dto.getNombreCompleto() != null ? dto.getNombreCompleto() : "Usuario del Sistema");
        usuario.setEstado(true);

        Set<Rol> roles = new HashSet<>();

        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            Rol defaultRole = rolRepository.findByNombre(RolNombre.ROLE_OPERADOR)
                    .orElseThrow(() -> new RuntimeException("Error: El rol ROLE_OPERADOR no existe en la base de datos."));
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

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y retorna el token JWT")
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
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\n  \"timestamp\": \"2026-10-03T21:45:23Z\",\n  \"status\": 400,\n  \"error\": \"Bad Request\",\n  \"message\": \"Los datos ingresados no son válidos\",\n  \"path\": \"/api/auth/login\"\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciales inválidas (usuario o contraseña incorrectos)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\n  \"timestamp\": \"2026-10-03T21:45:23Z\",\n  \"status\": 401,\n  \"error\": \"Unauthorized\",\n  \"message\": \"Credenciales inválidas. Verifique su usuario y contraseña.\",\n  \"path\": \"/api/auth/login\"\n}"
                )
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> loginUsuario(@Valid @RequestBody LoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generarToken(authentication);
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(dto.getUsername());

        String email = usuarioOpt.isPresent() ? usuarioOpt.get().getEmail() : "";
        JwtResponseDto respuesta = new JwtResponseDto(token, dto.getUsername(), email);

        return ResponseEntity.ok(respuesta);
    }
}