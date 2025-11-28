package com.ays.kardex.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Formato estándar para las respuestas de error del API")
public record ApiError(
        @Schema(description = "Código de estado HTTP asociado al error") int status,
        @Schema(description = "Mensaje de error legible para el cliente") String mensaje,
        @Schema(description = "Fecha y hora en la que ocurrió el error") LocalDateTime timestamp,
        @Schema(description = "Mapa opcional con detalles adicionales, como errores de validación") Map<String, String> detalles
) {
    public ApiError(int status, String mensaje, LocalDateTime timestamp) {
        this(status, mensaje, timestamp, null);
    }
}
