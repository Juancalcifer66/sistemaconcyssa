package com.concyssa.sistemaconcyssa.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con el token JWT e información del usuario")
public class JwtResponseDto {

    @Schema(description = "Token de autenticación JWT", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Nombre de usuario autenticado", example = "admin")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "admin@concyssa.com")
    private String email;
}
