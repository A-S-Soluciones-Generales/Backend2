package com.ays.kardex.controller;

import com.ays.kardex.dto.report.LowStockProductResponse;
import com.ays.kardex.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reportes", description = "Reportes operativos")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Listar productos bajo stock mínimo")
    public ResponseEntity<List<LowStockProductResponse>> obtenerBajoStock(
            @RequestParam(value = "sedeId", required = false) Long sedeId) {
        return ResponseEntity.ok(reportService.obtenerProductosBajoStock(sedeId));
    }
}
