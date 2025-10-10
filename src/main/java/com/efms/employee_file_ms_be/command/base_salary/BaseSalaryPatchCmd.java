package com.efms.employee_file_ms_be.command.base_salary;

import com.efms.employee_file_ms_be.api.request.BaseSalaryUpdateRequest;
import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.BaseSalaryNotFoundException;
import com.efms.employee_file_ms_be.model.domain.BaseSalary;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.mapper.base_salary.BaseSalaryMapper;
import com.efms.employee_file_ms_be.model.repository.BaseSalaryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class BaseSalaryPatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private BaseSalaryUpdateRequest baseSalaryUpdateRequest;

    @Getter
    private BaseSalaryResponse baseSalaryResponse;

    private final BaseSalaryRepository repository;

    private final BaseSalaryMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        BaseSalary baseSalary = repository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new BaseSalaryNotFoundException(id));
        updateProperties(baseSalary, baseSalaryUpdateRequest);
        baseSalary = repository.save(baseSalary);
        baseSalaryResponse = mapper.toDTO(baseSalary);
    }

    private void updateProperties(BaseSalary baseSalary, BaseSalaryUpdateRequest baseSalaryUpdateRequest) {
        Optional.ofNullable(baseSalaryUpdateRequest.getAmount()).ifPresent(baseSalary::setAmount);
        Optional.ofNullable(baseSalaryUpdateRequest.getStartDate()).ifPresent(baseSalary::setStartDate);
        Optional.ofNullable(baseSalaryUpdateRequest.getEndDate()).ifPresent(baseSalary::setEndDate);
        Optional.ofNullable(baseSalaryUpdateRequest.getEmployeeId()).ifPresent(employeeId -> {
            Employee employee = new Employee();
            employee.setId(UUID.fromString(employeeId));
            baseSalary.setEmployee(employee);
        });
    }
}
