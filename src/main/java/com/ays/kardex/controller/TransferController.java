package com.ays.kardex.controller;

import com.ays.kardex.dto.transfer.TransferInitiationRequest;
import com.ays.kardex.dto.transfer.TransferResponse;
import com.ays.kardex.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@Tag(name = "Transferencias", description = "API para gestionar transferencias de stock entre sedes")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/initiate")
    @Operation(summary = "Iniciar una transferencia de stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transferencia creada y stock descontado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o sin stock")
    })
    public ResponseEntity<TransferResponse> iniciarTransferencia(@Valid @RequestBody TransferInitiationRequest request) {
        TransferResponse response = transferService.iniciarTransferencia(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirmar la recepción de una transferencia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferencia confirmada y stock actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o sin autorización")
    })
    public ResponseEntity<TransferResponse> confirmarTransferencia(@PathVariable("id") Long id) {
        TransferResponse response = transferService.confirmarTransferencia(id);
        return ResponseEntity.ok(response);
    }
}
