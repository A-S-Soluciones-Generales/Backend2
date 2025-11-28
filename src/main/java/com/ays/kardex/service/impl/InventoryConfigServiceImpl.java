package com.ays.kardex.service.impl;

import com.ays.kardex.audit.AuditLogContext;
import com.ays.kardex.dto.inventory.InventoryConfigRequest;
import com.ays.kardex.dto.inventory.InventoryConfigResponse;
import com.ays.kardex.entity.Producto;
import com.ays.kardex.entity.Sede;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.repository.ProductoRepository;
import com.ays.kardex.service.InventoryConfigService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class InventoryConfigServiceImpl implements InventoryConfigService {

    private final ProductoRepository productoRepository;

    public InventoryConfigServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public InventoryConfigResponse configurarMinimo(InventoryConfigRequest request) {
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new BadRequestException("El producto indicado no existe"));

        Usuario usuario = obtenerUsuarioAutenticado();
        Sede sedeAsociada = validarSede(producto, request.getSedeId(), usuario);

        Integer minAnterior = producto.getMinStock();
        producto.setMinStock(request.getMinStock());
        productoRepository.save(producto);

        registrarAuditoria(producto, sedeAsociada, minAnterior, request.getMinStock());

        return InventoryConfigResponse.builder()
                .productoId(producto.getId())
                .sedeId(sedeAsociada.getId())
                .minStock(producto.getMinStock())
                .build();
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario usuario) {
            return usuario;
        }
        throw new BadRequestException("No se pudo identificar al usuario");
    }

    private Sede validarSede(Producto producto, Long sedeId, Usuario usuario) {
        Sede sedeProducto = producto.getSede();
        if (usuario.getRole() == Usuario.Role.VENDEDOR) {
            if (usuario.getSede() == null) {
                throw new BadRequestException("El vendedor no tiene una sede asignada");
            }
            if (sedeProducto == null || !usuario.getSede().getId().equals(sedeProducto.getId())) {
                throw new BadRequestException("El producto no pertenece a la sede del vendedor");
            }
            return usuario.getSede();
        }

        if (sedeId != null && (sedeProducto == null || !sedeId.equals(sedeProducto.getId()))) {
            throw new BadRequestException("El producto no pertenece a la sede indicada");
        }

        if (sedeProducto == null) {
            throw new BadRequestException("El producto no está asociado a una sede");
        }

        return sedeProducto;
    }

    private void registrarAuditoria(Producto producto, Sede sede, Integer minAnterior, Integer minNuevo) {
        Map<String, Object> detalles = new HashMap<>();
        detalles.put("productoId", producto.getId());
        detalles.put("sedeId", sede.getId());
        detalles.put("minStockAnterior", minAnterior);
        detalles.put("minStockNuevo", minNuevo);
        AuditLogContext.register("UPDATE_MIN_STOCK", detalles);
    }
}
