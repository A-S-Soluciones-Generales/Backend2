package com.ays.kardex.service.impl;

import com.ays.kardex.dto.company.CompanyRequest;
import com.ays.kardex.dto.company.CompanyResponse;
import com.ays.kardex.dto.company.CompanyStatusRequest;
import com.ays.kardex.dto.company.CompanyUpdateRequest;
import com.ays.kardex.dto.sede.SedeResponse;
import com.ays.kardex.entity.Company;
import com.ays.kardex.entity.Sede;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.exception.NotFoundException;
import com.ays.kardex.repository.CompanyRepository;
import com.ays.kardex.repository.SedeRepository;
import com.ays.kardex.service.CompanyService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final SedeRepository sedeRepository;

    @Override
    @Transactional
    public CompanyResponse crear(CompanyRequest request) {
        if (companyRepository.existsByRuc(request.getRuc())) {
            throw new BadRequestException("El RUC ya está registrado");
        }

        Company company = Company.builder()
                .nombre(request.getNombre())
                .ruc(request.getRuc())
                .razonSocial(request.getRazonSocial())
                .direccionFiscal(request.getDireccionFiscal())
                .telefono(request.getTelefono())
                .activo(true)
                .build();

        Company saved = companyRepository.save(company);

        return CompanyResponse.builder()
                .id(saved.getId())
                .nombre(saved.getNombre())
                .ruc(saved.getRuc())
                .razonSocial(saved.getRazonSocial())
                .direccionFiscal(saved.getDireccionFiscal())
                .telefono(saved.getTelefono())
                .activo(saved.getActivo())
                .build();
    }

    @Override
    @Transactional
    public CompanyResponse actualizar(Long id, CompanyUpdateRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La empresa indicada no existe"));

        company.setRazonSocial(request.getRazonSocial());
        company.setDireccionFiscal(request.getDireccionFiscal());
        company.setTelefono(request.getTelefono());

        Company updated = companyRepository.save(company);

        return CompanyResponse.builder()
                .id(updated.getId())
                .nombre(updated.getNombre())
                .ruc(updated.getRuc())
                .razonSocial(updated.getRazonSocial())
                .direccionFiscal(updated.getDireccionFiscal())
                .telefono(updated.getTelefono())
                .activo(updated.getActivo())
                .build();
    }

    @Override
    public CompanyResponse obtener(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La empresa indicada no existe"));

        return CompanyResponse.builder()
                .id(company.getId())
                .nombre(company.getNombre())
                .ruc(company.getRuc())
                .razonSocial(company.getRazonSocial())
                .direccionFiscal(company.getDireccionFiscal())
                .telefono(company.getTelefono())
                .activo(company.getActivo())
                .build();
    }

    @Override
    public Page<CompanyResponse> buscar(String nombre, String taxId, Pageable pageable) {
        return companyRepository.buscarPorNombreORuc(nombre, taxId, pageable)
                .map(company -> CompanyResponse.builder()
                        .id(company.getId())
                        .nombre(company.getNombre())
                        .ruc(company.getRuc())
                        .razonSocial(company.getRazonSocial())
                        .direccionFiscal(company.getDireccionFiscal())
                        .telefono(company.getTelefono())
                        .activo(company.getActivo())
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SedeResponse> listarSedes(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("La empresa indicada no existe"));

        return sedeRepository.findByCompanyId(company.getId())
                .stream()
                .map(this::mapearSede)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompanyResponse actualizarEstado(Long id, CompanyStatusRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La empresa indicada no existe"));

        company.setActivo(request.getActivo());
        Company actualizado = companyRepository.save(company);

        List<Sede> sedes = sedeRepository.findByCompanyId(actualizado.getId());
        sedes.forEach(sede -> sede.setActivo(request.getActivo()));
        sedeRepository.saveAll(sedes);

        return CompanyResponse.builder()
                .id(actualizado.getId())
                .nombre(actualizado.getNombre())
                .ruc(actualizado.getRuc())
                .razonSocial(actualizado.getRazonSocial())
                .direccionFiscal(actualizado.getDireccionFiscal())
                .telefono(actualizado.getTelefono())
                .activo(actualizado.getActivo())
                .build();
    }

    private SedeResponse mapearSede(Sede sede) {
        return SedeResponse.builder()
                .id(sede.getId())
                .nombre(sede.getNombre())
                .direccion(sede.getDireccion())
                .companyId(sede.getCompany() != null ? sede.getCompany().getId() : null)
                .activo(sede.getActivo())
                .build();
    }
}
