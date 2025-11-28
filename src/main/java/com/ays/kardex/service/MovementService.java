package com.ays.kardex.service;

import com.ays.kardex.dto.movement.MovementRequest;
import com.ays.kardex.dto.movement.MovementResponse;

import java.util.List;

public interface MovementService {

    MovementResponse registrarMovimiento(MovementRequest request);

    List<MovementResponse> obtenerMovimientosPorProducto(Long productoId, Long sedeId);
}
