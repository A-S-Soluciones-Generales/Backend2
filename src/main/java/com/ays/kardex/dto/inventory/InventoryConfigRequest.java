package com.ays.kardex.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para configurar el stock mínimo de un producto en una sede")
public class InventoryConfigRequest {

    @NotNull
    @Schema(description = "Identificador del producto", example = "1")
    private Long productoId;

    @Schema(description = "Identificador de la sede donde aplica la configuración", example = "2")
    private Long sedeId;

    @NotNull
    @Min(0)
    @Schema(description = "Cantidad mínima antes de alertar", example = "10")
    private Integer minStock;
}
