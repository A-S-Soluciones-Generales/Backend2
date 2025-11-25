package com.ays.kardex.service.impl;

import com.ays.kardex.dto.ProductoDTO;
import com.ays.kardex.entity.Producto;
import com.ays.kardex.repository.ProductoRepository;
import com.ays.kardex.service.ProductoService;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto crearProducto(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        return productoRepository.save(producto);
    }
}
