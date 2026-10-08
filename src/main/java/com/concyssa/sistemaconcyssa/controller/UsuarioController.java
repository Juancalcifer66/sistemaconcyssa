package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.dto.error.ErrorResponseDto;
import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioCreateDto;
import com.concyssa.sistemaconcyssa.dto.usuario.UsuarioResponseDto;
import com.concyssa.sistemaconcyssa.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Puntos finales para la administración de usuarios del sistema")
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener perfil del usuario autenticado", description = "Retorna los datos del usuario que ha iniciado sesión utilizando el DNI extraído del token JWT.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Perfil obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "No autorizado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)
            )
        )
    })
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTROLADOR', 'SUPERVISOR')")
    public ResponseEntity<UsuarioResponseDto> obtenerPerfilActual(Authentication authentication) {
        String dni = authentication.getName(); // El DNI se extrae directamente del token
        UsuarioResponseDto perfil = usuarioService.obtenerPorDni(dni);
        return ResponseEntity.ok(perfil);
    }

    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista con todos los usuarios registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
        @ApiResponse(
            responseCode = "401", 
            description = "No token o token inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(value = "{\n  \"timestamp\": \"2026-10-03T21:45:23Z\",\n  \"status\": 401,\n  \"error\": \"Unauthorized\",\n  \"message\": \"Acceso denegado. Se requiere un token válido.\",\n  \"path\": \"/api/usuarios\"\n}")
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Acceso denegado (requiere rol adecuado)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(value = "{\n  \"timestamp\": \"2026-10-03T21:45:23Z\",\n  \"status\": 403,\n  \"error\": \"Forbidden\",\n  \"message\": \"No tiene permisos suficientes para realizar esta acción.\",\n  \"path\": \"/api/usuarios\"\n}")
            )
        )
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @Operation(summary = "Listar controladores disponibles", description = "Obtiene una lista de todos los usuarios con rol de controlador activos para la asignación de órdenes.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de controladores obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/controladores")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<UsuarioResponseDto>> listarControladores() {
        return ResponseEntity.ok(usuarioService.listarControladores());
    }

    @Operation(summary = "Obtener detalles de un usuario por ID", description = "Retorna los datos detallados de un usuario específico según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(value = "{\n  \"timestamp\": \"2026-10-03T21:45:23Z\",\n  \"status\": 404,\n  \"error\": \"Not Found\",\n  \"message\": \"El usuario con ID 99 no fue encontrado.\",\n  \"path\": \"/api/usuarios/99\"\n}")
            )
        )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario asignando sus roles correspondientes.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos o nombre de usuario/email duplicado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(value = "{\n  \"timestamp\": \"2026-10-03T21:45:23Z\",\n  \"status\": 400,\n  \"error\": \"Bad Request\",\n  \"message\": \"El correo electrónico ya está registrado.\",\n  \"path\": \"/api/usuarios\"\n}")
            )
        )
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDto> crearUsuario(@Valid @RequestBody UsuarioCreateDto dto) {
        return new ResponseEntity<>(usuarioService.crearUsuario(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un usuario existente", description = "Modifica los datos de un usuario existente según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos inválidos o campos duplicados",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)
            )
        )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDto> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioCreateDto dto) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, dto));
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina de forma definitiva un usuario del sistema según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar estado de un usuario (activar/desactivar)", description = "Permite alternar el estado habilitado/inhabilitado de un usuario por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado cambiado exitosamente"),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(value = "{\n  \"timestamp\": \"2026-10-03T21:45:23Z\",\n  \"status\": 404,\n  \"error\": \"Not Found\",\n  \"message\": \"No se puede cambiar el estado. Usuario no encontrado.\",\n  \"path\": \"/api/usuarios/99/estado\"\n}")
            )
        )
    })
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDto> cambiarEstadoUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.cambiarEstado(id));
    }
}