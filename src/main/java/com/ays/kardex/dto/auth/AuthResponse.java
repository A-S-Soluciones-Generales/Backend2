package com.ays.kardex.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de autenticación con token JWT")
public class AuthResponse {

    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Tipo de token", example = "Bearer")
    private String tipo;

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Email del usuario", example = "usuario@email.com")
    private String email;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Schema(description = "Rol del usuario", example = "USER")
    private String role;

    @Schema(description = "Mensaje de respuesta", example = "Inicio de sesión exitoso")
    private String mensaje;
}
