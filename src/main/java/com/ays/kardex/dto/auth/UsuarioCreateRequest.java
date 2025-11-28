package com.ays.kardex.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioCreateRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del usuario", example = "Ana")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Schema(description = "Correo electrónico", example = "ana@email.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    @Schema(description = "Contraseña", example = "123456")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Schema(description = "Rol del usuario", example = "VENDEDOR")
    private String role;

    @Schema(description = "Empresa asignada", example = "1")
    private Long companyId;

    @Schema(description = "Sede asignada", example = "1")
    private Long sedeId;
}
