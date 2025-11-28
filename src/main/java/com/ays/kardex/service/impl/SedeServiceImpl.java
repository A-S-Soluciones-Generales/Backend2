package com.ays.kardex.service.impl;

import com.ays.kardex.dto.auth.UsuarioResponse;
import com.ays.kardex.dto.sede.SedeRequest;
import com.ays.kardex.dto.sede.SedeResponse;
import com.ays.kardex.dto.sede.SedeUpdateRequest;
import com.ays.kardex.entity.Company;
import com.ays.kardex.entity.Sede;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.exception.NotFoundException;
import com.ays.kardex.repository.CompanyRepository;
import com.ays.kardex.repository.ProductoRepository;
import com.ays.kardex.repository.SedeRepository;
import com.ays.kardex.repository.UsuarioRepository;
import com.ays.kardex.service.SedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SedeServiceImpl implements SedeService {

    private final CompanyRepository companyRepository;
    private final SedeRepository sedeRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public SedeResponse crear(SedeRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new BadRequestException("La empresa indicada no existe"));

        Sede sede = Sede.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .company(company)
                .activo(true)
                .build();

        Sede saved = sedeRepository.save(sede);

        return SedeResponse.builder()
                .id(saved.getId())
                .nombre(saved.getNombre())
                .direccion(saved.getDireccion())
                .companyId(company.getId())
                .activo(saved.getActivo())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SedeResponse obtener(Long id) {
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La sede indicada no existe"));

        validarAccesoVendedor(id);

        return mapearSede(sede);
    }

    @Override
    @Transactional
    public SedeResponse actualizar(Long id, SedeUpdateRequest request) {
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La sede indicada no existe"));

        sede.setNombre(request.getNombre());
        sede.setDireccion(request.getDireccion());

        Sede actualizado = sedeRepository.save(sede);

        return mapearSede(actualizado);
    }

    @Override
    @Transactional
    public void inactivar(Long id) {
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La sede indicada no existe"));

        Long stockTotal = productoRepository.calcularStockTotalPorSede(id);
        if (stockTotal != null && stockTotal > 0) {
            throw new BadRequestException("No se puede inactivar la sede porque aún tiene stock registrado");
        }

        sede.setActivo(false);
        sedeRepository.save(sede);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuariosActivos(Long sedeId) {
        Sede sede = sedeRepository.findById(sedeId)
                .orElseThrow(() -> new NotFoundException("La sede indicada no existe"));

        List<Usuario> usuarios = usuarioRepository.findBySedeIdAndActivoTrue(sede.getId());

        return usuarios.stream()
                .map(this::mapearUsuario)
                .collect(Collectors.toList());
    }

    private void validarAccesoVendedor(Long sedeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario usuario) {
            if (usuario.getRole() == Usuario.Role.VENDEDOR) {
                Long sedeAsignada = usuario.getSede() != null ? usuario.getSede().getId() : null;
                if (!sedeId.equals(sedeAsignada)) {
                    throw new AccessDeniedException("El vendedor solo puede consultar su propia sede");
                }
            }
        }
    }

    private SedeResponse mapearSede(Sede sede) {
        return SedeResponse.builder()
                .id(sede.getId())
                .nombre(sede.getNombre())
                .direccion(sede.getDireccion())
                .companyId(sede.getCompany().getId())
                .activo(sede.getActivo())
                .build();
    }

    private UsuarioResponse mapearUsuario(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .documento(usuario.getDocumento())
                .role(usuario.getRole().name())
                .companyId(usuario.getCompany() != null ? usuario.getCompany().getId() : null)
                .sedeId(usuario.getSede() != null ? usuario.getSede().getId() : null)
                .sedeNombre(usuario.getSede() != null ? usuario.getSede().getNombre() : null)
                .activo(usuario.getActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }
}
