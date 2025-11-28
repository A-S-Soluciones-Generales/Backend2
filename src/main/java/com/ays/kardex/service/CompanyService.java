package com.ays.kardex.service;

import com.ays.kardex.dto.company.CompanyRequest;
import com.ays.kardex.dto.company.CompanyResponse;
import com.ays.kardex.dto.company.CompanyStatusRequest;
import com.ays.kardex.dto.company.CompanyUpdateRequest;
import com.ays.kardex.dto.sede.SedeResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {

    CompanyResponse crear(CompanyRequest request);

    CompanyResponse actualizar(Long id, CompanyUpdateRequest request);

    CompanyResponse obtener(Long id);

    Page<CompanyResponse> buscar(String nombre, String taxId, Pageable pageable);

    List<SedeResponse> listarSedes(Long companyId);

    CompanyResponse actualizarEstado(Long id, CompanyStatusRequest request);
}
