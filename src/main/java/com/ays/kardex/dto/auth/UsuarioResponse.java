package com.ays.kardex.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Información pública del usuario")
public class UsuarioResponse {

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Email del usuario", example = "usuario@email.com")
    private String email;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Schema(description = "Rol del usuario", example = "VENDEDOR")
    private String role;

    @Schema(description = "Empresa asociada", example = "1")
    private Long companyId;

    @Schema(description = "Sede asociada", example = "2")
    private Long sedeId;

    @Schema(description = "Estado de la cuenta", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaCreacion;
}
