package com.ays.kardex.dto.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Producto que se encuentra por debajo del stock mínimo")
public class LowStockProductResponse {

    @Schema(description = "Identificador del producto", example = "5")
    private Long productoId;

    @Schema(description = "Nombre del producto", example = "Coca Cola 500ml")
    private String nombre;

    @Schema(description = "Identificador de la sede", example = "1")
    private Long sedeId;

    @Schema(description = "Nombre de la sede", example = "Sucursal Centro")
    private String sedeNombre;

    @Schema(description = "Cantidad actual en stock", example = "8")
    private Integer stockActual;

    @Schema(description = "Stock mínimo configurado", example = "15")
    private Integer minStock;
}
