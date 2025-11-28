package com.ays.kardex.dto.sede;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SedeUpdateRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre de la sede", example = "Almacén Central")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Schema(description = "Dirección física", example = "Av. Siempre Viva 123")
    private String direccion;
}
