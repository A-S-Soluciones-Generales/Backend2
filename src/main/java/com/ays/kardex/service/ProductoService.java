package com.ays.kardex.service;

import com.ays.kardex.dto.ProductoDTO;
import com.ays.kardex.entity.Producto;

public interface ProductoService {
    Producto crearProducto(ProductoDTO productoDTO);
}
