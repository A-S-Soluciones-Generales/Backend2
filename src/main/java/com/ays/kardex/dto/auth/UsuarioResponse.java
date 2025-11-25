package com.ays.kardex.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del usuario actual")
public class UsuarioResponse {

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Correo electrónico", example = "juan@email.com")
    private String email;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Schema(description = "Rol del usuario", example = "USER")
    private String role;

    @Schema(description = "Estado de la cuenta", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de creación de la cuenta")
    private LocalDateTime fechaCreacion;
}
