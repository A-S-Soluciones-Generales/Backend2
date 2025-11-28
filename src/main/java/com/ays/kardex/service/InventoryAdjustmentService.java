package com.ays.kardex.service;

import com.ays.kardex.dto.adjustment.InventoryAdjustmentRequest;
import com.ays.kardex.dto.adjustment.InventoryAdjustmentResponse;

public interface InventoryAdjustmentService {

    InventoryAdjustmentResponse registrarAjuste(InventoryAdjustmentRequest request);
}
