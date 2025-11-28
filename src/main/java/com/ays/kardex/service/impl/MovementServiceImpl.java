package com.ays.kardex.service.impl;

import com.ays.kardex.dto.movement.MovementRequest;
import com.ays.kardex.dto.movement.MovementResponse;
import com.ays.kardex.entity.Movement;
import com.ays.kardex.entity.Producto;
import com.ays.kardex.entity.Sede;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.repository.MovementRepository;
import com.ays.kardex.repository.ProductoRepository;
import com.ays.kardex.repository.SedeRepository;
import com.ays.kardex.service.MovementService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final ProductoRepository productoRepository;
    private final SedeRepository sedeRepository;

    public MovementServiceImpl(MovementRepository movementRepository, ProductoRepository productoRepository,
                               SedeRepository sedeRepository) {
        this.movementRepository = movementRepository;
        this.productoRepository = productoRepository;
        this.sedeRepository = sedeRepository;
    }

    @Override
    public MovementResponse registrarMovimiento(MovementRequest request) {
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new BadRequestException("El producto indicado no existe"));

        Usuario usuario = obtenerUsuarioAutenticado();
        Sede sedeMovimiento = resolverSedeMovimiento(request.getSedeId(), usuario, producto);

        if (producto.getSede() != null && !producto.getSede().getId().equals(sedeMovimiento.getId())) {
            throw new BadRequestException("El producto no pertenece a la sede indicada");
        }

        int cantidad = request.getCantidad();
        int nuevoStock = calcularNuevoStock(producto.getStock(), cantidad, request.getTipo());
        producto.setStock(nuevoStock);
        productoRepository.save(producto);

        Movement movement = new Movement();
        movement.setProducto(producto);
        movement.setSede(sedeMovimiento);
        movement.setQuantity(cantidad);
        movement.setType(request.getTipo());
        movement.setStockResulting(nuevoStock);

        Movement guardado = movementRepository.save(movement);
        return mapearRespuesta(guardado);
    }

    @Override
    public List<MovementResponse> obtenerMovimientosPorProducto(Long productoId, Long sedeId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new BadRequestException("El producto indicado no existe"));

        Usuario usuario = obtenerUsuarioAutenticado();
        Long sedeConsulta = resolverSedeConsulta(sedeId, usuario, producto);

        List<Movement> movimientos;
        if (sedeConsulta != null) {
            movimientos = movementRepository.findByProductoIdAndSedeIdOrderByFechaCreacionDesc(productoId, sedeConsulta);
        } else {
            movimientos = movementRepository.findByProductoIdOrderByFechaCreacionDesc(productoId);
        }

        return movimientos.stream()
                .map(this::mapearRespuesta)
                .collect(Collectors.toList());
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
                    .orElseThrow(() -> new BadRequestException("La sede indicada no existe"));
        }

        if (producto.getSede() != null) {
            return producto.getSede();
        }

        throw new BadRequestException("Debe indicar una sede para registrar el movimiento");
    }

    private Long resolverSedeConsulta(Long sedeId, Usuario usuario, Producto producto) {
        if (usuario.getRole() == Usuario.Role.VENDEDOR) {
            if (usuario.getSede() == null) {
                throw new BadRequestException("El vendedor no tiene una sede asignada");
            }
            if (producto.getSede() != null && !Objects.equals(producto.getSede().getId(), usuario.getSede().getId())) {
                throw new BadRequestException("El producto no pertenece a la sede del vendedor");
            }
            return usuario.getSede().getId();
        }

        if (sedeId != null) {
            if (producto.getSede() != null && !Objects.equals(producto.getSede().getId(), sedeId)) {
                throw new BadRequestException("El producto no pertenece a la sede solicitada");
            }
            return sedeId;
        }

        return producto.getSede() != null ? producto.getSede().getId() : null;
    }

    private int calcularNuevoStock(int stockActual, int cantidad, Movement.MovementType tipo) {
        if (tipo == Movement.MovementType.COMPRA) {
            return stockActual + cantidad;
        }
        if (stockActual < cantidad) {
            throw new BadRequestException("Stock insuficiente para la venta");
        }
        return stockActual - cantidad;
    }

    private MovementResponse mapearRespuesta(Movement movement) {
        return MovementResponse.builder()
                .id(movement.getId())
                .tipo(movement.getType())
                .cantidad(movement.getQuantity())
                .stockResultante(movement.getStockResulting())
                .productoId(movement.getProducto().getId())
                .sedeId(movement.getSede().getId())
                .fechaCreacion(movement.getFechaCreacion())
                .build();
    }
}
