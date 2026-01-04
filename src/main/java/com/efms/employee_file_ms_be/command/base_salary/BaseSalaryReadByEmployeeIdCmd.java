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

@CommandExecute
@RequiredArgsConstructor
public class BaseSalaryReadByEmployeeIdCmd implements Command {

    @Setter
    private String employeeId;

    @Getter
    private BaseSalaryResponse baseSalaryResponse;

    private final BaseSalaryRepository repository;

    private final BaseSalaryMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        BaseSalary baseSalary = repository.findByEmployeeIdAndCompanyId(UUID.fromString(employeeId), companyId)
                .orElseThrow(BaseSalaryNotFoundException::new);

        baseSalaryResponse = mapper.toDTO(baseSalary);
    }
}
