package com.efms.employee_file_ms_be.model.mapper.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeCreateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.model.domain.*;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmployeeMapper implements CustomMapper<EmployeeResponse, EmployeeCreateRequest, Employee> {

    @Override
    public EmployeeResponse toDTO(Employee employee) {
        return EmployeeResponse.builder()
                .id(String.valueOf(employee.getId()))
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .address(employee.getAddress())
                .birthDate(employee.getBirthDate())
                .hireDate(employee.getHireDate())
                .status(employee.getStatus().name())
                .departmentId(employee.getDepartment().getId().toString())
                .departmentName(employee.getDepartment().getName())
                .positionId(employee.getPosition().getId().toString())
                .positionName(employee.getPosition().getName())
                .locationId(employee.getLocation().getId().toString())
                .locationName(employee.getLocation().getName())
                .companyId(employee.getCompany().getId().toString())
                .build();
    }

    @Override
    public Employee toEntity(EmployeeCreateRequest employeeCreateRequest) {
        Department department = new Department();
        Position position = new Position();
        Location location = new Location();
        Company company = new Company();
        department.setId(UUID.fromString(employeeCreateRequest.getDepartmentId()));
        position.setId(UUID.fromString(employeeCreateRequest.getPositionId()));
        location.setId(UUID.fromString(employeeCreateRequest.getLocationId()));
        company.setId(UUID.fromString(employeeCreateRequest.getCompanyId()));
        return Employee.builder()
                .firstName(employeeCreateRequest.getFirstName())
                .lastName(employeeCreateRequest.getLastName())
                .email(employeeCreateRequest.getEmail())
                .phone(employeeCreateRequest.getPhone())
                .address(employeeCreateRequest.getAddress())
                .birthDate(employeeCreateRequest.getBirthDate())
                .hireDate(employeeCreateRequest.getHireDate())
                .department(department)
                .position(position)
                .location(location)
                .company(company)
                .status(EmployeeStatus.ACTIVE)
                .isDeleted(false)
                .build();
    }
}
