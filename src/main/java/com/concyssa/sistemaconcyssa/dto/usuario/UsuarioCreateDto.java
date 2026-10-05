package com.concyssa.sistemaconcyssa.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "Objeto necesario para crear un nuevo usuario")
public class UsuarioCreateDto {

    @Schema(description = "Nombre de usuario para inicio de sesión", example = "jperez")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = "El usuario debe tener entre 4 y 20 caracteres")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@concyssa.com")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe proporcionar un formato de correo valido")
    private String email;

    @Schema(description = "Contraseña de acceso", example = "123456")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @Schema(description = "Conjunto de roles asignados", example = "[\"ROLE_ADMIN\"]")
    @NotEmpty(message = "Debe asignar al menos un rol al usuario")
    private Set<String> roles;
}