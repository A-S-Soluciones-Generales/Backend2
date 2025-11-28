package com.ays.kardex.service.impl;

import com.ays.kardex.dto.ProductoDTO;
import com.ays.kardex.entity.Producto;
import com.ays.kardex.entity.Sede;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.repository.ProductoRepository;
import com.ays.kardex.repository.SedeRepository;
import com.ays.kardex.service.ProductoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final SedeRepository sedeRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository, SedeRepository sedeRepository) {
        this.productoRepository = productoRepository;
        this.sedeRepository = sedeRepository;
    }

    @Override
    public Producto crearProducto(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setMinStock(productoDTO.getMinStock());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario usuario) {
            if (usuario.getRole() == Usuario.Role.VENDEDOR) {
                producto.setSede(usuario.getSede());
            } else if (productoDTO.getSedeId() != null) {
                Sede sede = sedeRepository.findById(productoDTO.getSedeId())
                        .orElseThrow(() -> new BadRequestException("La sede indicada no existe"));
                producto.setSede(sede);
            }
        }

        return productoRepository.save(producto);
    }
}
