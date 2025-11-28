package com.ays.kardex.service.impl;

import com.ays.kardex.audit.AuditLogContext;
import com.ays.kardex.dto.adjustment.InventoryAdjustmentRequest;
import com.ays.kardex.dto.adjustment.InventoryAdjustmentResponse;
import com.ays.kardex.entity.InventoryAdjustment;
import com.ays.kardex.entity.Producto;
import com.ays.kardex.entity.Sede;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.exception.NotFoundException;
import com.ays.kardex.repository.InventoryAdjustmentRepository;
import com.ays.kardex.repository.ProductoRepository;
import com.ays.kardex.repository.SedeRepository;
import com.ays.kardex.service.InventoryAdjustmentService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class InventoryAdjustmentServiceImpl implements InventoryAdjustmentService {

    private final InventoryAdjustmentRepository inventoryAdjustmentRepository;
    private final ProductoRepository productoRepository;
    private final SedeRepository sedeRepository;

    public InventoryAdjustmentServiceImpl(InventoryAdjustmentRepository inventoryAdjustmentRepository,
                                          ProductoRepository productoRepository,
                                          SedeRepository sedeRepository) {
        this.inventoryAdjustmentRepository = inventoryAdjustmentRepository;
        this.productoRepository = productoRepository;
        this.sedeRepository = sedeRepository;
    }

    @Override
    public InventoryAdjustmentResponse registrarAjuste(InventoryAdjustmentRequest request) {
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new NotFoundException("El producto indicado no existe"));

        Usuario usuario = obtenerUsuarioAutenticado();
        Sede sedeMovimiento = resolverSedeMovimiento(request.getSedeId(), usuario, producto);

        if (producto.getSede() != null && !producto.getSede().getId().equals(sedeMovimiento.getId())) {
            throw new BadRequestException("El producto no pertenece a la sede indicada");
        }

        int stockActual = producto.getStock();
        int nuevoStock = calcularNuevoStock(stockActual, request.getCantidad(), request.getTipo());
        BigDecimal valorImpacto = calcularImpactoValor(producto, request.getCantidad(), request.getTipo());

        producto.setStock(nuevoStock);
        productoRepository.save(producto);

        InventoryAdjustment adjustment = new InventoryAdjustment();
        adjustment.setProducto(producto);
        adjustment.setSede(sedeMovimiento);
        adjustment.setQuantity(request.getCantidad());
        adjustment.setType(request.getTipo());
        adjustment.setReasonCode(request.getReasonCode());
        adjustment.setNote(request.getNote());
        adjustment.setValueImpact(valorImpacto.doubleValue());
        adjustment.setStockResulting(nuevoStock);

        InventoryAdjustment guardado = inventoryAdjustmentRepository.save(adjustment);
        registrarAuditoria(producto, sedeMovimiento, request, stockActual, nuevoStock);
        return mapearRespuesta(guardado);
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario usuario) {
            return usuario;
        }
        throw new BadRequestException("No se pudo identificar al usuario");
    }

    private Sede resolverSedeMovimiento(Long sedeId, Usuario usuario, Producto producto) {
        if (usuario.getRole() == Usuario.Role.VENDEDOR) {
            if (usuario.getSede() == null) {
                throw new BadRequestException("El vendedor no tiene una sede asignada");
            }
            return usuario.getSede();
        }

        if (sedeId != null) {
            return sedeRepository.findById(sedeId)
                    .orElseThrow(() -> new NotFoundException("La sede indicada no existe"));
        }

        if (producto.getSede() != null) {
            return producto.getSede();
        }

        throw new BadRequestException("Debe indicar una sede para registrar el ajuste");
    }

    private int calcularNuevoStock(int stockActual, int cantidad, InventoryAdjustment.AdjustmentType tipo) {
        if (tipo == InventoryAdjustment.AdjustmentType.POSITIVE) {
            return stockActual + cantidad;
        }
        if (stockActual < cantidad) {
            throw new BadRequestException("Stock insuficiente para registrar la merma");
        }
        return stockActual - cantidad;
    }

    private BigDecimal calcularImpactoValor(Producto producto, int cantidad, InventoryAdjustment.AdjustmentType tipo) {
        BigDecimal costoPromedio = BigDecimal.valueOf(producto.getAverageCost() != null ? producto.getAverageCost() : 0.0);
        BigDecimal valor = costoPromedio.multiply(BigDecimal.valueOf(cantidad)).setScale(4, RoundingMode.HALF_UP);
        return tipo == InventoryAdjustment.AdjustmentType.NEGATIVE ? valor.negate() : valor;
    }

    private InventoryAdjustmentResponse mapearRespuesta(InventoryAdjustment adjustment) {
        return InventoryAdjustmentResponse.builder()
                .id(adjustment.getId())
                .tipo(adjustment.getType())
                .cantidad(adjustment.getQuantity())
                .stockResultante(adjustment.getStockResulting())
                .reasonCode(adjustment.getReasonCode())
                .note(adjustment.getNote())
                .valorImpacto(adjustment.getValueImpact())
                .productoId(adjustment.getProducto().getId())
                .sedeId(adjustment.getSede().getId())
                .fechaCreacion(adjustment.getFechaCreacion())
                .build();
    }

    private void registrarAuditoria(Producto producto, Sede sede, InventoryAdjustmentRequest request,
                                    int stockAnterior, int stockNuevo) {
        Map<String, Object> detalles = new HashMap<>();
        detalles.put("productoId", producto.getId());
        detalles.put("sedeId", sede.getId());
        detalles.put("stockAnterior", stockAnterior);
        detalles.put("stockNuevo", stockNuevo);
        detalles.put("cantidad", request.getCantidad());
        detalles.put("tipo", request.getTipo());
        detalles.put("reasonCode", request.getReasonCode());
        AuditLogContext.register("CREATE_ADJUSTMENT", detalles);
    }
}
