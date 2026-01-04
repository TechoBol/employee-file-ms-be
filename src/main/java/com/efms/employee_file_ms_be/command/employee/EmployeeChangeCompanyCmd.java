package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.EmployeeNotFoundException;
import com.efms.employee_file_ms_be.model.domain.BaseSalary;
import com.efms.employee_file_ms_be.model.domain.ChangeType;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.domain.File;
import com.efms.employee_file_ms_be.model.mapper.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import com.efms.employee_file_ms_be.service.EmployeeHistoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class EmployeeChangeCompanyCmd implements Command {

    @Setter
    private UUID employeeId;

    @Setter
    private UUID newCompanyId;

    @Setter
    private String newCompanyName;

    @Setter
    private String userName;

    @Setter
    private String reason;

    @Getter
    private EmployeeResponse response;

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    private final EmployeeHistoryService historyService;

    @Transactional
    @Override
    public void execute() {
        UUID currentCompanyId = UUID.fromString(TenantContext.getTenantId());

        Employee employee = repository.findByIdAndCompanyId(employeeId, currentCompanyId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId.toString()));

        employee.setBranch(null);
        employee.setPosition(null);

        BaseSalary employeeSalary = employee.getBaseSalary();
        if (employeeSalary != null) {
            employeeSalary.setCompanyId(newCompanyId);
        }

        File employeeFile = employee.getFile();
        if (employeeFile != null) {
            employeeFile.setCompanyId(newCompanyId);
        }

        employee.setCompanyId(newCompanyId);
        employee = repository.save(employee);

        String eventDescription = String.format(
                "Cambio de Empresa a %s%s",
                newCompanyName,
                reason != null ? ". Razón: " + reason : ""
        );

        historyService.saveEmployeeHistoryAsync(
                employee,
                ChangeType.COMPANY_CHANGE,
                userName,
                eventDescription
        );

        response = mapper.toDTO(employee);
    }
}
