package com.ays.kardex.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyStatusRequest {

    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "Estado de la empresa", example = "false")
    private Boolean activo;
}
