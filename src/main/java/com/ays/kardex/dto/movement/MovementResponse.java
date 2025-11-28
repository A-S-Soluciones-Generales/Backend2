package com.ays.kardex.dto.movement;

import com.ays.kardex.entity.Movement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con el detalle de un movimiento de inventario")
public class MovementResponse {

    private Long id;
    private Movement.MovementType tipo;
    private Integer cantidad;
    private Integer stockResultante;
    private Long productoId;
    private Long sedeId;
    private LocalDateTime fechaCreacion;
}
