package com.ays.kardex.service.impl;

import com.ays.kardex.dto.auth.UsuarioCreateRequest;
import com.ays.kardex.dto.auth.UsuarioResponse;
import com.ays.kardex.entity.Company;
import com.ays.kardex.entity.Sede;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.repository.CompanyRepository;
import com.ays.kardex.repository.SedeRepository;
import com.ays.kardex.repository.UsuarioRepository;
import com.ays.kardex.service.UsuarioService;
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

        Usuario.Role role;
        try {
            role = Usuario.Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Rol no válido. Use GLOBAL_ADMIN o VENDEDOR");
        }

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
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .company(company)
                .sede(sede)
                .activo(true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        return UsuarioResponse.builder()
                .id(guardado.getId())
                .email(guardado.getEmail())
                .nombre(guardado.getNombre())
                .apellido(guardado.getApellido())
                .role(guardado.getRole().name())
                .companyId(guardado.getCompany() != null ? guardado.getCompany().getId() : null)
                .sedeId(guardado.getSede() != null ? guardado.getSede().getId() : null)
                .activo(guardado.getActivo())
                .fechaCreacion(guardado.getFechaCreacion())
                .build();
    }
}
