package com.efms.employee_file_ms_be.command.company;

import com.efms.employee_file_ms_be.api.response.CompanyResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.repository.CompanyRepository;
import com.efms.employee_file_ms_be.model.repository.projection.CompanyProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@CommandExecute
@RequiredArgsConstructor
public class CompanyListCmd implements Command {

    @Getter
    private List<CompanyResponse> companies;

    private final CompanyRepository repository;

    @Override
    public void execute() {
        List<CompanyProjection> allCompanies = repository.findAllCompaniesProjection();
        companies = allCompanies.stream()
                .map(this::toResponse)
                .toList();
    }

    private CompanyResponse toResponse(CompanyProjection company) {
        return CompanyResponse.builder()
                .id(company.getId().toString())
                .name(company.getName())
                .build();
    }
}
