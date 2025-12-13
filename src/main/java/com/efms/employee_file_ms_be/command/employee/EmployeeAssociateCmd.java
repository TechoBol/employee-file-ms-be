package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.EmployeeNotFoundException;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.domain.EmployeeStatus;
import com.efms.employee_file_ms_be.model.mapper.employee.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class EmployeeAssociateCmd implements Command {

    @Setter
    private String id;

    @Getter
    private EmployeeResponse employeeResponse;

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Employee employee = repository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employee.setIsDisassociated(false);
        employee.setDisassociatedAt(null);
        employee.setDisassociationDate(null);
        employee.setDisassociationReason(null);
        employee.setStatus(EmployeeStatus.ACTIVE);

        employee = repository.save(employee);
        employeeResponse = mapper.toDTO(employee);
    }
}
