package com.ays.kardex.dto.adjustment;

import com.ays.kardex.entity.InventoryAdjustment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para registrar un ajuste de inventario")
public class InventoryAdjustmentRequest {

    @NotNull
    @Schema(description = "Identificador del producto", example = "1")
    private Long productoId;

    @NotNull
    @Schema(description = "Tipo de ajuste", example = "NEGATIVE")
    private InventoryAdjustment.AdjustmentType tipo;

    @NotNull
    @Min(1)
    @Schema(description = "Cantidad a ajustar", example = "3")
    private Integer cantidad;

    @NotBlank
    @Schema(description = "Código de razón del ajuste", example = "DAMAGED")
    private String reasonCode;

    @NotBlank
    @Schema(description = "Detalle o nota del ajuste", example = "Producto golpeado en almacén")
    private String note;

    @Schema(description = "Identificador de la sede. Se ignora para vendedores", example = "2")
    private Long sedeId;
}
