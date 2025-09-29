package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeUpdateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.*;
import com.efms.employee_file_ms_be.model.mapper.employee.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Optionals;

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
        Employee employee = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        updateProperties(employee, employeeUpdateRequest);
        employee = repository.save(employee);
        employeeResponse = mapper.toDTO(employee);
    }

    private void updateProperties(Employee employee, EmployeeUpdateRequest employeeUpdateRequest) {
        Optional.ofNullable(employeeUpdateRequest.getFirstName()).ifPresent(employee::setFirstName);
        Optional.ofNullable(employeeUpdateRequest.getLastName()).ifPresent(employee::setLastName);
        Optional.ofNullable(employeeUpdateRequest.getEmail()).ifPresent(employee::setEmail);
        Optional.ofNullable(employeeUpdateRequest.getPhone()).ifPresent(employee::setPhone);
        Optional.ofNullable(employeeUpdateRequest.getAddress()).ifPresent(employee::setAddress);
        Optional.ofNullable(employeeUpdateRequest.getBirthDate()).ifPresent(employee::setBirthDate);
        Optional.ofNullable(employeeUpdateRequest.getHireDate()).ifPresent(employee::setHireDate);
        Optional.ofNullable(employeeUpdateRequest.getStatus()).ifPresent(
                status -> employee.setStatus(EmployeeStatus.valueOf(status))
        );
        Optional.ofNullable(employeeUpdateRequest.getEmergencyContact()).ifPresent(employee::setEmergencyContact);
        Optional.ofNullable(employeeUpdateRequest.getDepartmentId()).ifPresent(departmentId -> {
            Department department = new  Department();
            department.setId(UUID.fromString(departmentId));
            employee.setDepartment(department);
        });
        Optional.ofNullable(employeeUpdateRequest.getPositionId()).ifPresent(positionId -> {
            Position position = new Position();
            position.setId(UUID.fromString(positionId));
            employee.setPosition(position);
        });
        Optional.ofNullable(employeeUpdateRequest.getLocationId()).ifPresent(locationId -> {
            Location location = new Location();
            location.setId(UUID.fromString(locationId));
            employee.setLocation(location);
        });
        Optional.ofNullable(employeeUpdateRequest.getCompanyId()).ifPresent(companyId -> {
            Company company = new Company();
            company.setId(UUID.fromString(companyId));
            employee.setCompany(company);
        });
    }
}
