package com.ays.kardex.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompanyUpdateRequest {

    @NotBlank(message = "La razón social es obligatoria")
    @Schema(description = "Razón social registrada", example = "AYS SOLUTIONS S.A.")
    private String razonSocial;

    @NotBlank(message = "La dirección fiscal es obligatoria")
    @Schema(description = "Dirección fiscal de la empresa", example = "Av. Principal 123")
    private String direccionFiscal;

    @NotBlank(message = "El teléfono es obligatorio")
    @Schema(description = "Teléfono de contacto", example = "+51 999 888 777")
    private String telefono;
}
