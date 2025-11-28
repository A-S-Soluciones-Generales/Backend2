package com.ays.kardex.service;

import com.ays.kardex.dto.report.LowStockProductResponse;
import java.util.List;

public interface ReportService {

    List<LowStockProductResponse> obtenerProductosBajoStock(Long sedeId);
}
