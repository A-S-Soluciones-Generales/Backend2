package com.ays.kardex.service.impl;

import com.ays.kardex.dto.transfer.TransferInitiationRequest;
import com.ays.kardex.dto.transfer.TransferResponse;
import com.ays.kardex.entity.Producto;
import com.ays.kardex.entity.Sede;
import com.ays.kardex.entity.Transfer;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.repository.ProductoRepository;
import com.ays.kardex.repository.SedeRepository;
import com.ays.kardex.repository.TransferRepository;
import com.ays.kardex.service.TransferService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final ProductoRepository productoRepository;
    private final SedeRepository sedeRepository;

    public TransferServiceImpl(TransferRepository transferRepository, ProductoRepository productoRepository,
                               SedeRepository sedeRepository) {
        this.transferRepository = transferRepository;
        this.productoRepository = productoRepository;
        this.sedeRepository = sedeRepository;
    }

    @Override
    public TransferResponse iniciarTransferencia(TransferInitiationRequest request) {
        Usuario usuario = obtenerUsuarioAutenticado();
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new BadRequestException("El producto indicado no existe"));

        Sede sedeOrigen = sedeRepository.findById(request.getSourceSedeId())
                .orElseThrow(() -> new BadRequestException("La sede de origen indicada no existe"));
        Sede sedeDestino = sedeRepository.findById(request.getDestinationSedeId())
                .orElseThrow(() -> new BadRequestException("La sede de destino indicada no existe"));

        if (sedeOrigen.getId().equals(sedeDestino.getId())) {
            throw new BadRequestException("La sede de origen y destino no pueden ser iguales");
        }

        validarSedeProducto(producto, sedeOrigen);
        validarPermisosOrigen(usuario, sedeOrigen);

        if (producto.getStock() < request.getCantidad()) {
            throw new BadRequestException("Stock insuficiente en la sede de origen");
        }

        producto.setStock(producto.getStock() - request.getCantidad());
        productoRepository.save(producto);

        Transfer transferencia = new Transfer();
        transferencia.setProducto(producto);
        transferencia.setSourceSede(sedeOrigen);
        transferencia.setDestinationSede(sedeDestino);
        transferencia.setQuantity(request.getCantidad());
        transferencia.setStatus(Transfer.TransferStatus.IN_TRANSIT);

        Transfer guardada = transferRepository.save(transferencia);
        return mapearRespuesta(guardada);
    }

    @Override
    public TransferResponse confirmarTransferencia(Long transferId) {
        Usuario usuario = obtenerUsuarioAutenticado();
        Transfer transferencia = transferRepository.findById(transferId)
                .orElseThrow(() -> new BadRequestException("La transferencia indicada no existe"));

        if (transferencia.getStatus() != Transfer.TransferStatus.IN_TRANSIT) {
            throw new BadRequestException("La transferencia ya fue procesada");
        }

        validarPermisosDestino(usuario, transferencia.getDestinationSede());

        Producto producto = transferencia.getProducto();
        producto.setStock(producto.getStock() + transferencia.getQuantity());
        productoRepository.save(producto);

        transferencia.setStatus(Transfer.TransferStatus.COMPLETED);
        Transfer guardada = transferRepository.save(transferencia);

        return mapearRespuesta(guardada);
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario usuario) {
            return usuario;
        }
        throw new BadRequestException("No se pudo identificar al usuario");
    }

    private void validarSedeProducto(Producto producto, Sede sedeOrigen) {
        if (producto.getSede() != null && !producto.getSede().getId().equals(sedeOrigen.getId())) {
            throw new BadRequestException("El producto no pertenece a la sede de origen indicada");
        }
    }

    private void validarPermisosOrigen(Usuario usuario, Sede sedeOrigen) {
        if (usuario.getRole() == Usuario.Role.GLOBAL_ADMIN) {
            return;
        }
        if (usuario.getSede() == null || !usuario.getSede().getId().equals(sedeOrigen.getId())) {
            throw new BadRequestException("El usuario no tiene permisos para solicitar transferencias desde esta sede");
        }
    }

    private void validarPermisosDestino(Usuario usuario, Sede sedeDestino) {
        if (usuario.getRole() == Usuario.Role.GLOBAL_ADMIN) {
            return;
        }
        if (usuario.getSede() == null || !usuario.getSede().getId().equals(sedeDestino.getId())) {
            throw new BadRequestException("El usuario no tiene permisos para confirmar transferencias a esta sede");
        }
    }

    private TransferResponse mapearRespuesta(Transfer transferencia) {
        return TransferResponse.builder()
                .id(transferencia.getId())
                .productoId(transferencia.getProducto().getId())
                .sourceSedeId(transferencia.getSourceSede().getId())
                .destinationSedeId(transferencia.getDestinationSede().getId())
                .cantidad(transferencia.getQuantity())
                .estado(transferencia.getStatus())
                .fechaCreacion(transferencia.getFechaCreacion())
                .build();
    }
}
