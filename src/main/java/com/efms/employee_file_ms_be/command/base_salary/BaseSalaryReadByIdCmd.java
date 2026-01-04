package com.efms.employee_file_ms_be.command.base_salary;

import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.BaseSalaryNotFoundException;
import com.efms.employee_file_ms_be.model.domain.BaseSalary;
import com.efms.employee_file_ms_be.model.mapper.BaseSalaryMapper;
import com.efms.employee_file_ms_be.model.repository.BaseSalaryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class BaseSalaryReadByIdCmd implements Command {

    @Setter
    private String id;

    @Getter
    private BaseSalaryResponse baseSalaryResponse;

    private final BaseSalaryRepository repository;

    private final BaseSalaryMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        BaseSalary baseSalary = repository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new BaseSalaryNotFoundException(id));
        baseSalaryResponse = mapper.toDTO(baseSalary);
    }
}
