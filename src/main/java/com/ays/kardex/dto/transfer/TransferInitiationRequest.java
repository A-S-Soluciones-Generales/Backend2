package com.ays.kardex.dto.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para iniciar una transferencia de stock")
public class TransferInitiationRequest {

    @NotNull
    @Schema(description = "Identificador del producto", example = "1")
    private Long productoId;

    @NotNull
    @Schema(description = "Identificador de la sede de origen", example = "1")
    private Long sourceSedeId;

    @NotNull
    @Schema(description = "Identificador de la sede de destino", example = "2")
    private Long destinationSedeId;

    @NotNull
    @Min(1)
    @Schema(description = "Cantidad a transferir", example = "5")
    private Integer cantidad;
}
