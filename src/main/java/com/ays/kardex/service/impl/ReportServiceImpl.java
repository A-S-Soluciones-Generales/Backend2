package com.ays.kardex.service.impl;

import com.ays.kardex.dto.report.LowStockProductResponse;
import com.ays.kardex.entity.Producto;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.repository.ProductoRepository;
import com.ays.kardex.service.ReportService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    private final ProductoRepository productoRepository;

    public ReportServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<LowStockProductResponse> obtenerProductosBajoStock(Long sedeId) {
        Usuario usuario = obtenerUsuarioAutenticado();
        Long sedeFiltro = resolverSede(usuario, sedeId);

        List<Producto> productos = productoRepository.buscarBajoStockPorSede(sedeFiltro);

        return productos.stream()
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

    private Long resolverSede(Usuario usuario, Long sedeSolicitada) {
        if (usuario.getRole() == Usuario.Role.VENDEDOR) {
            if (usuario.getSede() == null) {
                throw new BadRequestException("El vendedor no tiene una sede asignada");
            }
            return usuario.getSede().getId();
        }
        return sedeSolicitada;
    }

    private LowStockProductResponse mapearRespuesta(Producto producto) {
        return LowStockProductResponse.builder()
                .productoId(producto.getId())
                .nombre(producto.getNombre())
                .sedeId(producto.getSede() != null ? producto.getSede().getId() : null)
                .sedeNombre(producto.getSede() != null ? producto.getSede().getNombre() : null)
                .stockActual(producto.getStock())
                .minStock(producto.getMinStock())
                .build();
    }
}
