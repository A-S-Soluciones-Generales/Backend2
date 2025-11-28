package com.ays.kardex.controller;

import com.ays.kardex.dto.inventory.InventoryConfigRequest;
import com.ays.kardex.dto.inventory.InventoryConfigResponse;
import com.ays.kardex.service.InventoryConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventario", description = "Configuraciones de inventario")
public class InventoryConfigController {

    private final InventoryConfigService inventoryConfigService;

    public InventoryConfigController(InventoryConfigService inventoryConfigService) {
        this.inventoryConfigService = inventoryConfigService;
    }

    @PutMapping("/config")
    @Operation(summary = "Configurar stock mínimo por producto y sede")
    public ResponseEntity<InventoryConfigResponse> configurarMinimo(
            @Valid @RequestBody InventoryConfigRequest request) {
        return ResponseEntity.ok(inventoryConfigService.configurarMinimo(request));
    }
}
