package com.ays.kardex.service;

import com.ays.kardex.dto.ProductoDTO;
import com.ays.kardex.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoService {
    Producto crearProducto(ProductoDTO productoDTO);

    Producto actualizarProducto(Long id, ProductoDTO productoDTO);

    Producto actualizarProductoParcial(Long id, ProductoDTO productoDTO);

    void inactivarProducto(Long id);

    Page<Producto> buscarProductos(String query, Pageable pageable);
}
