package com.ays.kardex.service.impl;

import com.ays.kardex.dto.ProductoDTO;
import com.ays.kardex.entity.Producto;
import com.ays.kardex.entity.Sede;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.exception.NotFoundException;
import com.ays.kardex.repository.ProductoRepository;
import com.ays.kardex.repository.SedeRepository;
import com.ays.kardex.service.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final SedeRepository sedeRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository, SedeRepository sedeRepository) {
        this.productoRepository = productoRepository;
        this.sedeRepository = sedeRepository;
    }

    @Override
    @Transactional
    public Producto crearProducto(ProductoDTO productoDTO) {
        if (productoDTO.getSku() == null || productoDTO.getSku().isBlank()) {
            throw new BadRequestException("El SKU es obligatorio");
        }
        if (productoRepository.existsBySku(productoDTO.getSku())) {
            throw new BadRequestException("El SKU ya está registrado");
        }

        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setSku(productoDTO.getSku());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setMinStock(productoDTO.getMinStock());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario usuario) {
            if (usuario.getRole() == Usuario.Role.VENDEDOR) {
                producto.setSede(usuario.getSede());
            } else if (productoDTO.getSedeId() != null) {
                Sede sede = sedeRepository.findById(productoDTO.getSedeId())
                        .orElseThrow(() -> new NotFoundException("La sede indicada no existe"));
                producto.setSede(sede);
            }
        }

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto actualizarProducto(Long id, ProductoDTO productoDTO) {
        Producto producto = obtenerProductoPorId(id);

        if (productoDTO.getSku() == null || productoDTO.getSku().isBlank()) {
            throw new BadRequestException("El SKU es obligatorio");
        }
        validarSkuUnico(productoDTO.getSku(), producto.getId());

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setSku(productoDTO.getSku());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setMinStock(productoDTO.getMinStock());

        if (productoDTO.getSedeId() != null) {
            Sede sede = sedeRepository.findById(productoDTO.getSedeId())
                    .orElseThrow(() -> new NotFoundException("La sede indicada no existe"));
            producto.setSede(sede);
        }

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto actualizarProductoParcial(Long id, ProductoDTO productoDTO) {
        Producto producto = obtenerProductoPorId(id);

        if (productoDTO.getSku() != null) {
            validarSkuUnico(productoDTO.getSku(), producto.getId());
            producto.setSku(productoDTO.getSku());
        }
        if (productoDTO.getNombre() != null) {
            producto.setNombre(productoDTO.getNombre());
        }
        if (productoDTO.getDescripcion() != null) {
            producto.setDescripcion(productoDTO.getDescripcion());
        }
        if (productoDTO.getPrecio() != null) {
            producto.setPrecio(productoDTO.getPrecio());
        }
        if (productoDTO.getStock() != null) {
            producto.setStock(productoDTO.getStock());
        }
        if (productoDTO.getMinStock() != null) {
            producto.setMinStock(productoDTO.getMinStock());
        }
        if (productoDTO.getSedeId() != null) {
            Sede sede = sedeRepository.findById(productoDTO.getSedeId())
                    .orElseThrow(() -> new NotFoundException("La sede indicada no existe"));
            producto.setSede(sede);
        }

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void inactivarProducto(Long id) {
        Producto producto = obtenerProductoPorId(id);
        if (producto.getStock() != null && producto.getStock() > 0) {
            throw new BadRequestException("Product has stock");
        }
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    public Page<Producto> buscarProductos(String query, Pageable pageable) {
        String filtro = query == null ? "" : query;
        return productoRepository.buscarPorNombreOSku(filtro, pageable);
    }

    private Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El producto indicado no existe"));
    }

    private void validarSkuUnico(String sku, Long idActual) {
        if (productoRepository.existsBySkuAndIdNot(sku, idActual)) {
            throw new BadRequestException("El SKU ya está registrado");
        }
        if (productoRepository.existsBySku(sku) && idActual == null) {
            throw new BadRequestException("El SKU ya está registrado");
        }
    }
}
