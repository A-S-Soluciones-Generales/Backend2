package com.ays.kardex.dto.sede;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SedeResponse {

    @Schema(description = "Identificador de la sede", example = "1")
    private Long id;

    @Schema(description = "Nombre de la sede", example = "Almacén Central")
    private String nombre;

    @Schema(description = "Dirección física", example = "Av. Siempre Viva 123")
    private String direccion;

    @Schema(description = "Empresa a la que pertenece", example = "1")
    private Long companyId;

    @Schema(description = "Estado de la sede", example = "true")
    private Boolean activo;
}
