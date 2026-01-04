package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeUpdateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.EmployeeNotFoundException;
import com.efms.employee_file_ms_be.model.domain.ChangeType;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.domain.EmployeeStatus;
import com.efms.employee_file_ms_be.model.mapper.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import com.efms.employee_file_ms_be.service.EmployeeHistoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
@Slf4j
public class EmployeeDisassociateCmd implements Command {

    @Setter
    private String id;

    @Setter
    private EmployeeUpdateRequest employeeUpdateRequest;

    @Setter
    private String userName;

    @Getter
    private EmployeeResponse employeeResponse;

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    private final EmployeeHistoryService historyService;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Employee employee = repository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        log.info("Disassociating employee: {} from company: {}", id, companyId);

        employee.setIsDisassociated(true);
        employee.setDisassociatedAt(LocalDateTime.now());
        employee.setStatus(EmployeeStatus.INACTIVE);

        Optional.ofNullable(employeeUpdateRequest.getDisassociationDate()).ifPresent(employee::setDisassociationDate);
        Optional.ofNullable(employeeUpdateRequest.getDisassociationReason()).ifPresent(employee::setDisassociationReason);

        employee = repository.save(employee);
        employeeResponse = mapper.toDTO(employee);

        String reason = String.format("Empleado desvinculado. Razón: %s",
                employeeUpdateRequest.getDisassociationReason() != null
                        ? employeeUpdateRequest.getDisassociationReason()
                        : "No especificada");

        historyService.saveEmployeeHistoryAsync(
                employee,
                ChangeType.DISASSOCIATE,
                userName,
                reason
        );

        log.info("Employee {} successfully disassociated", id);
    }
}
