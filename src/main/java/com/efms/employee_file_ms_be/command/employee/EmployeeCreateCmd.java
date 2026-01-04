package com.efms.employee_file_ms_be.command.employee;


import com.efms.employee_file_ms_be.api.request.EmployeeCreateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.Constants;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.ChangeType;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.mapper.employee.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import com.efms.employee_file_ms_be.service.EmployeeHistoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@CommandExecute
@RequiredArgsConstructor
public class EmployeeCreateCmd implements Command {

    @Setter
    private EmployeeCreateRequest employeeCreateRequest;

    @Setter
    private boolean includeDetails;

    @Setter
    private String userName;

    @Getter
    private EmployeeResponse response;

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    private final EmployeeHistoryService historyService;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Employee employee = mapper.toEntity(employeeCreateRequest);
        employee.setCompanyId(companyId);
        employee = repository.save(employee);

        historyService.saveEmployeeHistoryAsync(
                employee,
                ChangeType.CREATE,
                userName,
                Constants.HistoryEvents.EMPLOYEE_CREATE
        );

        if (includeDetails) {
            employee = repository.findByIdAndCompanyId(employee.getId(), companyId)
                    .orElse(employee);
        }
        response = mapper.toDTO(employee);
    }
}
