package com.ays.kardex.service;

import com.ays.kardex.dto.inventory.InventoryConfigRequest;
import com.ays.kardex.dto.inventory.InventoryConfigResponse;

public interface InventoryConfigService {

    InventoryConfigResponse configurarMinimo(InventoryConfigRequest request);
}
