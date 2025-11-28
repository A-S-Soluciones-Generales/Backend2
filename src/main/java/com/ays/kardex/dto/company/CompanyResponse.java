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

    @Schema(description = "Estado de la empresa", example = "true")
    private Boolean activo;
}
