package com.ays.kardex.dto.movement;

import com.ays.kardex.entity.Movement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para registrar un movimiento de inventario")
public class MovementRequest {

    @NotNull
    @Schema(description = "Identificador del producto", example = "1")
    private Long productoId;

    @NotNull
    @Schema(description = "Tipo de movimiento", example = "COMPRA")
    private Movement.MovementType tipo;

    @NotNull
    @Min(1)
    @Schema(description = "Cantidad de unidades", example = "5")
    private Integer cantidad;

    @Schema(description = "Identificador de la sede. Se ignora para vendedores", example = "2")
    private Long sedeId;
}
