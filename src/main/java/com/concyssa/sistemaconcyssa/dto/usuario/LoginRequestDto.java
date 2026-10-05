package com.concyssa.sistemaconcyssa.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Credenciales para inicio de sesión")
public class LoginRequestDto {

    @Schema(description = "Nombre de usuario", example = "admin")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @Schema(description = "Contraseña de acceso", example = "123456")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
