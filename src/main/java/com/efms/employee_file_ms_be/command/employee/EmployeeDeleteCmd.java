package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.EmployeeNotFoundException;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@CommandExecute
@RequiredArgsConstructor
public class EmployeeDeleteCmd implements Command {

    @Setter
    private String id;

    private final EmployeeRepository employeeRepository;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Employee employee = employeeRepository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        employee.setIsDeleted(true);
        employee.setDeletedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }
}
