package com.concyssa.sistemaconcyssa.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDto {

    private Long id;
    private String username;
    private String email;
    private String nombreCompleto;
    private boolean estado;
    private Set<String> roles;
}
