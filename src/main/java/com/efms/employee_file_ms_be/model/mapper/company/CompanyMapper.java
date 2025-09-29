package com.efms.employee_file_ms_be.model.mapper.company;

import com.efms.employee_file_ms_be.api.request.CompanyCreateRequest;
import com.efms.employee_file_ms_be.api.response.CompanyResponse;
import com.efms.employee_file_ms_be.model.domain.Company;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper implements CustomMapper<CompanyResponse, CompanyCreateRequest, Company> {

    @Override
    public Company toEntity(CompanyCreateRequest companyCreateRequest) {
        Company instance = new Company();
        BeanUtils.copyProperties(companyCreateRequest, instance);
        return instance;
    }

    @Override
    public CompanyResponse toDTO(Company company) {
        return CompanyResponse.builder()
                .id(company.getId().toString())
                .name(company.getName())
                .build();
    }
}
