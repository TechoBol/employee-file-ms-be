package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.EmployeeNotFoundException;
import com.efms.employee_file_ms_be.model.domain.ChangeType;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.mapper.employee.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import com.efms.employee_file_ms_be.service.EmployeeHistoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    private String userName;

    @Setter
    private String reason;

    @Getter
    private EmployeeResponse response;

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    private final EmployeeHistoryService historyService;

    @Override
    public void execute() {
        UUID currentCompanyId = UUID.fromString(TenantContext.getTenantId());

        Employee employee = repository.findByIdAndCompanyId(employeeId, currentCompanyId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        employee.setBranch(null);
        employee.setPosition(null);

        employee.setCompanyId(newCompanyId);
        employee = repository.save(employee);

        String eventDescription = String.format(
                "Company changed from %s to %s%s",
                currentCompanyId,
                newCompanyId,
                reason != null ? ". Reason: " + reason : ""
        );

        historyService.saveEmployeeHistoryAsync(
                employee,
                ChangeType.UPDATE,
                userName,
                eventDescription
        );

        response = mapper.toDTO(employee);
    }
}
