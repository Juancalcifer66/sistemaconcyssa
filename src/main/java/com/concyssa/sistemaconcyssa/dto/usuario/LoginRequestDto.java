package com.concyssa.sistemaconcyssa.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Credenciales para inicio de sesión basado en DNI")
public class LoginRequestDto {

    @Schema(description = "DNI del usuario", example = "71234567")
    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 dígitos")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI solo debe contener números enteros, sin espacios ni letras")
    private String dni;

    @Schema(description = "Contraseña de acceso", example = "123456")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}