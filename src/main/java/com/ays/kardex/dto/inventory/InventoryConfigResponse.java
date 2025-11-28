package com.ays.kardex.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con la configuración de stock mínimo aplicada")
public class InventoryConfigResponse {

    @Schema(description = "Identificador del producto", example = "1")
    private Long productoId;

    @Schema(description = "Identificador de la sede donde aplica la configuración", example = "2")
    private Long sedeId;

    @Schema(description = "Stock mínimo configurado", example = "25")
    private Integer minStock;
}
