package com.ays.kardex.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear o actualizar un producto")
public class ProductoDTO {

    @Schema(description = "Nombre del producto", example = "Laptop HP")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Laptop HP con 16GB RAM")
    private String descripcion;

    @Schema(description = "Precio del producto", example = "1500.00")
    private Double precio;

    @Schema(description = "Cantidad en stock", example = "50")
    private Integer stock;

    @Schema(description = "Identificador de la sede", example = "1")
    private Long sedeId;
}
