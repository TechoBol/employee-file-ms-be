package com.efms.employee_file_ms_be.command.company;

import com.efms.employee_file_ms_be.api.request.CompanyCreateRequest;
import com.efms.employee_file_ms_be.api.response.CompanyResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Company;
import com.efms.employee_file_ms_be.model.mapper.company.CompanyMapper;
import com.efms.employee_file_ms_be.model.repository.CompanyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@CommandExecute
@RequiredArgsConstructor
public class CompanyCreateCmd implements Command {

    @Setter
    private CompanyCreateRequest companyCreateRequest;

    @Getter
    private CompanyResponse companyResponse;

    private final CompanyRepository repository;

    private final CompanyMapper mapper;

    @Override
    public void execute() {
        Company company = repository.save(mapper.toEntity(companyCreateRequest));
        companyResponse = mapper.toDTO(company);
    }
}
