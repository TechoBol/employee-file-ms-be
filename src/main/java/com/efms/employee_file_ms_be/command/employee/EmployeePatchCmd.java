package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeUpdateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.EmployeeNotFoundException;
import com.efms.employee_file_ms_be.model.domain.*;
import com.efms.employee_file_ms_be.model.mapper.employee.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

@CommandExecute
@RequiredArgsConstructor
public class EmployeePatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private EmployeeUpdateRequest employeeUpdateRequest;

    @Getter
    private EmployeeResponse employeeResponse;

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Employee employee = repository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        updateProperties(employee, employeeUpdateRequest);
        employee = repository.save(employee);
        employeeResponse = mapper.toDTO(employee);
    }

    private void updateProperties(Employee employee, EmployeeUpdateRequest employeeUpdateRequest) {
        Optional.ofNullable(employeeUpdateRequest.getFirstName()).ifPresent(employee::setFirstName);
        Optional.ofNullable(employeeUpdateRequest.getLastName()).ifPresent(employee::setLastName);
        Optional.ofNullable(employeeUpdateRequest.getCi()).ifPresent(employee::setCi);
        Optional.ofNullable(employeeUpdateRequest.getEmail()).ifPresent(employee::setEmail);
        Optional.ofNullable(employeeUpdateRequest.getPhone()).ifPresent(employee::setPhone);
        Optional.ofNullable(employeeUpdateRequest.getAddress()).ifPresent(employee::setAddress);
        Optional.ofNullable(employeeUpdateRequest.getBirthDate()).ifPresent(employee::setBirthDate);
        Optional.ofNullable(employeeUpdateRequest.getHireDate()).ifPresent(employee::setHireDate);
        Optional.ofNullable(employeeUpdateRequest.getStatus()).ifPresent(
                status -> employee.setStatus(EmployeeStatus.valueOf(status))
        );
        Optional.ofNullable(employeeUpdateRequest.getEmergencyContact()).ifPresent(employee::setEmergencyContact);
        Optional.ofNullable(employeeUpdateRequest.getPositionId()).ifPresent(positionId -> {
            Position position = new Position();
            position.setId(UUID.fromString(positionId));
            employee.setPosition(position);
        });
        Optional.ofNullable(employeeUpdateRequest.getDisassociationDate()).ifPresent(employee::setDisassociationDate);
        Optional.ofNullable(employeeUpdateRequest.getDisassociationReason()).ifPresent(employee::setDisassociationReason);
    }
}
