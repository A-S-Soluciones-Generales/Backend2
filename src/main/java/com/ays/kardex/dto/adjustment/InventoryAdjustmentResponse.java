package com.ays.kardex.dto.adjustment;

import com.ays.kardex.entity.InventoryAdjustment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con el detalle de un ajuste de inventario")
public class InventoryAdjustmentResponse {

    private Long id;
    private InventoryAdjustment.AdjustmentType tipo;
    private Integer cantidad;
    private Integer stockResultante;
    private String reasonCode;
    private String note;
    private Double valorImpacto;
    private Long productoId;
    private Long sedeId;
    private LocalDateTime fechaCreacion;
}
