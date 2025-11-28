package com.ays.kardex.controller;

import com.ays.kardex.dto.adjustment.InventoryAdjustmentRequest;
import com.ays.kardex.dto.adjustment.InventoryAdjustmentResponse;
import com.ays.kardex.service.InventoryAdjustmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Ajustes de Inventario", description = "API para registrar mermas y sobrantes")
public class InventoryAdjustmentController {

    private final InventoryAdjustmentService inventoryAdjustmentService;

    public InventoryAdjustmentController(InventoryAdjustmentService inventoryAdjustmentService) {
        this.inventoryAdjustmentService = inventoryAdjustmentService;
    }

    @PostMapping("/adjustments")
    @Operation(summary = "Registrar un ajuste de inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ajuste registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<InventoryAdjustmentResponse> registrarAjuste(
            @Valid @RequestBody InventoryAdjustmentRequest request) {
        InventoryAdjustmentResponse response = inventoryAdjustmentService.registrarAjuste(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
