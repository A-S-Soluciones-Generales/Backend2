package com.ays.kardex.controller;

import com.ays.kardex.dto.ProductoDTO;
import com.ays.kardex.entity.Producto;
import com.ays.kardex.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/products", "/api/productos"})
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Producto> crearProducto(@RequestBody ProductoDTO productoDTO) {
        Producto nuevoProducto = productoService.crearProducto(productoDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto", description = "Actualiza todos los campos de un producto existente")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        Producto actualizado = productoService.actualizarProducto(id, productoDTO);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un producto", description = "Actualiza solo los campos enviados del producto")
    public ResponseEntity<Producto> actualizarProductoParcial(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        Producto actualizado = productoService.actualizarProductoParcial(id, productoDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inactivar un producto", description = "Desactiva un producto si no tiene stock disponible")
    public ResponseEntity<Void> inactivarProducto(@PathVariable Long id) {
        productoService.inactivarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Buscar productos", description = "Busca productos por nombre o SKU con coincidencias parciales")
    public ResponseEntity<Page<Producto>> buscarProductos(@RequestParam(name = "search", required = false) String search,
                                                          Pageable pageable) {
        Page<Producto> productos = productoService.buscarProductos(search, pageable);
        return ResponseEntity.ok(productos);
    }
}
