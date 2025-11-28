package com.ays.kardex.service.impl;

import com.ays.kardex.dto.sede.SedeRequest;
import com.ays.kardex.dto.sede.SedeResponse;
import com.ays.kardex.entity.Company;
import com.ays.kardex.entity.Sede;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.repository.CompanyRepository;
import com.ays.kardex.repository.SedeRepository;
import com.ays.kardex.service.SedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SedeServiceImpl implements SedeService {

    private final CompanyRepository companyRepository;
    private final SedeRepository sedeRepository;

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
}
