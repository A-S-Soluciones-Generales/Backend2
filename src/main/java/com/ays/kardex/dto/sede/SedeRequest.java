package com.ays.kardex.dto.sede;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SedeRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre de la sede", example = "Almacén Central")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Schema(description = "Dirección física", example = "Av. Siempre Viva 123")
    private String direccion;

    @NotNull(message = "El company_id es obligatorio")
    @Schema(description = "Identificador de la empresa", example = "1")
    private Long companyId;
}
