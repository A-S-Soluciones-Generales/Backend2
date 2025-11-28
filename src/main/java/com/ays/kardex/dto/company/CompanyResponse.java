package com.ays.kardex.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyResponse {

    @Schema(description = "Identificador interno", example = "1")
    private Long id;

    @Schema(description = "Nombre comercial", example = "Ays Solutions")
    private String nombre;

    @Schema(description = "RUC o ID fiscal", example = "20123456789")
    private String ruc;

    @Schema(description = "Razón social registrada", example = "AYS SOLUTIONS S.A.")
    private String razonSocial;

    @Schema(description = "Dirección fiscal", example = "Av. Principal 123")
    private String direccionFiscal;

    @Schema(description = "Teléfono de contacto", example = "+51 999 888 777")
    private String telefono;

    @Schema(description = "Estado de la empresa", example = "true")
    private Boolean activo;
}
