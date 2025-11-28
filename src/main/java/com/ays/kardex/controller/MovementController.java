package com.ays.kardex.controller;

import com.ays.kardex.dto.movement.MovementRequest;
import com.ays.kardex.dto.movement.MovementResponse;
import com.ays.kardex.service.MovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Movimientos", description = "API para registrar y consultar movimientos de inventario")
public class MovementController {

    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @PostMapping("/movements")
    @Operation(summary = "Registrar un movimiento de inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<MovementResponse> registrarMovimiento(@Valid @RequestBody MovementRequest request) {
        MovementResponse response = movementService.registrarMovimiento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/products/{id}/movements")
    @Operation(summary = "Listar movimientos de un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<List<MovementResponse>> obtenerMovimientos(@PathVariable("id") Long productoId,
                                                                     @RequestParam(value = "sede_id", required = false) Long sedeId) {
        List<MovementResponse> movimientos = movementService.obtenerMovimientosPorProducto(productoId, sedeId);
        return ResponseEntity.ok(movimientos);
    }
}
