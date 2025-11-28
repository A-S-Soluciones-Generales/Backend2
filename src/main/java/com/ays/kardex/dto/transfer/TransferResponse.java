package com.ays.kardex.dto.transfer;

import com.ays.kardex.entity.Transfer;
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
@Schema(description = "Respuesta de operaciones de transferencia de stock")
public class TransferResponse {

    @Schema(description = "Identificador de la transferencia", example = "10")
    private Long id;

    @Schema(description = "Identificador del producto", example = "1")
    private Long productoId;

    @Schema(description = "Sede de origen", example = "1")
    private Long sourceSedeId;

    @Schema(description = "Sede de destino", example = "2")
    private Long destinationSedeId;

    @Schema(description = "Cantidad transferida", example = "5")
    private Integer cantidad;

    @Schema(description = "Estado actual de la transferencia", example = "IN_TRANSIT")
    private Transfer.TransferStatus estado;

    @Schema(description = "Fecha de creación de la transferencia")
    private LocalDateTime fechaCreacion;
}
