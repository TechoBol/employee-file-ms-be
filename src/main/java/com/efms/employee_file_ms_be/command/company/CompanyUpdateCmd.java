package com.efms.employee_file_ms_be.command.company;

import com.efms.employee_file_ms_be.api.request.CompanyUpdateRequest;
import com.efms.employee_file_ms_be.api.response.CompanyResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.exception.CompanyNotFoundException;
import com.efms.employee_file_ms_be.model.domain.Company;
import com.efms.employee_file_ms_be.model.mapper.CompanyMapper;
import com.efms.employee_file_ms_be.model.repository.CompanyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class CompanyUpdateCmd implements Command {

    @Setter
    private String id;

    @Setter
    private CompanyUpdateRequest request;

    @Getter
    private CompanyResponse companyResponse;

    private final CompanyRepository repository;

    private final CompanyMapper mapper;

    @Override
    public void execute() {
        Company company = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new CompanyNotFoundException(id));
        updateProperties(company, request);
        company = repository.save(company);
        companyResponse = mapper.toDTO(company);
    }

    private void updateProperties(Company company, CompanyUpdateRequest request) {
        company.setName(request.getName());
    }
}
