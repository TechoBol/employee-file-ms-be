package com.efms.employee_file_ms_be.command.company;

import com.efms.employee_file_ms_be.api.response.CompanyResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Company;
import com.efms.employee_file_ms_be.model.mapper.CompanyMapper;
import com.efms.employee_file_ms_be.model.repository.CompanyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@CommandExecute
@RequiredArgsConstructor
public class CompanyListCmd implements Command {

    @Getter
    private List<CompanyResponse> companies;

    private final CompanyRepository repository;

    private final CompanyMapper mapper;

    @Override
    public void execute() {
        List<Company> allCompanies = repository.findAll();
        companies = allCompanies.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
