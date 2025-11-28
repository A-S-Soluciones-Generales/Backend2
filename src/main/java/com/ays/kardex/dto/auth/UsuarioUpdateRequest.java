package com.ays.kardex.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioUpdateRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del usuario", example = "Ana")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Schema(description = "Documento de identidad", example = "87654321")
    private String documento;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Schema(description = "Correo electrónico", example = "ana@email.com")
    private String email;

    @NotBlank(message = "El rol es obligatorio")
    @Schema(description = "Rol del usuario", example = "VENDEDOR")
    private String role;

    @Schema(description = "Empresa asignada", example = "1")
    private Long companyId;

    @Schema(description = "Sede asignada", example = "1")
    private Long sedeId;
}
