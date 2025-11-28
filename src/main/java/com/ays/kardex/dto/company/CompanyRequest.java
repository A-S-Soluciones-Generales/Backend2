package com.ays.kardex.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompanyRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre comercial de la empresa", example = "Ays Solutions")
    private String nombre;

    @NotBlank(message = "El RUC es obligatorio")
    @Schema(description = "Identificador fiscal único", example = "20123456789")
    private String ruc;
}
