package com.ays.kardex.service.impl;

import com.ays.kardex.dto.auth.UsuarioCreateRequest;
import com.ays.kardex.dto.auth.UsuarioResponse;
import com.ays.kardex.dto.auth.UsuarioUpdateRequest;
import com.ays.kardex.entity.Company;
import com.ays.kardex.entity.Sede;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.exception.NotFoundException;
import com.ays.kardex.repository.CompanyRepository;
import com.ays.kardex.repository.SedeRepository;
import com.ays.kardex.repository.UsuarioRepository;
import com.ays.kardex.service.UsuarioService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CompanyRepository companyRepository;
    private final SedeRepository sedeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponse crearUsuario(UsuarioCreateRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        Usuario.Role role = parsearRol(request.getRole());

        Company company = null;
        Sede sede = null;

        if (role == Usuario.Role.VENDEDOR) {
            if (request.getCompanyId() == null || request.getSedeId() == null) {
                throw new BadRequestException("Los vendedores deben incluir company_id y sede_id");
            }

            if (!sedeRepository.existsByIdAndCompanyId(request.getSedeId(), request.getCompanyId())) {
                throw new BadRequestException("La sede indicada no pertenece a la empresa enviada");
            }

            company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new BadRequestException("La empresa indicada no existe"));
            sede = sedeRepository.findById(request.getSedeId())
                    .orElseThrow(() -> new BadRequestException("La sede indicada no existe"));
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .documento(request.getDocumento())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .company(company)
                .sede(sede)
                .activo(true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        return mapearUsuario(guardado);
    }

    @Override
    @Transactional
    public UsuarioResponse actualizarUsuario(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El usuario indicado no existe"));

        if (!usuario.getEmail().equalsIgnoreCase(request.getEmail())
                && usuarioRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new BadRequestException("El email ya está registrado");
        }

        Usuario.Role nuevoRol = parsearRol(request.getRole());

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setDocumento(request.getDocumento());
        usuario.setRole(nuevoRol);

        if (nuevoRol == Usuario.Role.VENDEDOR) {
            Long companyId = request.getCompanyId() != null ? request.getCompanyId()
                    : (usuario.getCompany() != null ? usuario.getCompany().getId() : null);

            if (companyId == null || request.getSedeId() == null) {
                throw new BadRequestException("Los vendedores deben incluir company_id y sede_id");
            }

            if (!sedeRepository.existsByIdAndCompanyId(request.getSedeId(), companyId)) {
                throw new BadRequestException("La nueva sede indicada no pertenece a la empresa enviada");
            }

            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new BadRequestException("La empresa indicada no existe"));
            Sede sede = sedeRepository.findById(request.getSedeId())
                    .orElseThrow(() -> new BadRequestException("La sede indicada no existe"));

            usuario.setCompany(company);
            usuario.setSede(sede);
        } else {
            usuario.setCompany(null);
            usuario.setSede(null);
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return mapearUsuario(actualizado);
    }

    @Override
    @Transactional
    public void inactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El usuario indicado no existe"));

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarUsuarios(String search) {
        List<Usuario> usuarios;
        if (search == null || search.isBlank()) {
            usuarios = usuarioRepository.findAll();
        } else {
            usuarios = usuarioRepository.buscarPorTermino(search.trim());
        }

        return usuarios.stream()
                .map(this::mapearUsuario)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuariosPorCompany(Long companyId) {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("La empresa indicada no existe"));

        List<Usuario> usuarios = usuarioRepository.findByCompanyIdAndActivoTrue(companyId);
        return usuarios.stream()
                .map(this::mapearUsuario)
                .collect(Collectors.toList());
    }

    private Usuario.Role parsearRol(String rol) {
        try {
            return Usuario.Role.valueOf(rol.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Rol no válido. Use GLOBAL_ADMIN o VENDEDOR");
        }
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
