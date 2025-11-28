package com.ays.kardex.service.impl;

import com.ays.kardex.dto.company.CompanyRequest;
import com.ays.kardex.dto.company.CompanyResponse;
import com.ays.kardex.entity.Company;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.repository.CompanyRepository;
import com.ays.kardex.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public CompanyResponse crear(CompanyRequest request) {
        if (companyRepository.existsByRuc(request.getRuc())) {
            throw new BadRequestException("El RUC ya está registrado");
        }

        Company company = Company.builder()
                .nombre(request.getNombre())
                .ruc(request.getRuc())
                .activo(true)
                .build();

        Company saved = companyRepository.save(company);

        return CompanyResponse.builder()
                .id(saved.getId())
                .nombre(saved.getNombre())
                .ruc(saved.getRuc())
                .activo(saved.getActivo())
                .build();
    }
}
