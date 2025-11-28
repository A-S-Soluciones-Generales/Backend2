package com.ays.kardex.service;

import com.ays.kardex.dto.company.CompanyRequest;
import com.ays.kardex.dto.company.CompanyResponse;

public interface CompanyService {

    CompanyResponse crear(CompanyRequest request);
}
