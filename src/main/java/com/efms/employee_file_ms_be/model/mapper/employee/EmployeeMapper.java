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
                .ci(employee.getCi())
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
                .build();
    }

    @Override
    public Employee toEntity(EmployeeCreateRequest employeeCreateRequest) {
        Department department = new Department();
        Position position = new Position();
        department.setId(UUID.fromString(employeeCreateRequest.getDepartmentId()));
        position.setId(UUID.fromString(employeeCreateRequest.getPositionId()));
        return Employee.builder()
                .firstName(employeeCreateRequest.getFirstName())
                .lastName(employeeCreateRequest.getLastName())
                .ci(employeeCreateRequest.getCi())
                .email(employeeCreateRequest.getEmail())
                .phone(employeeCreateRequest.getPhone())
                .address(employeeCreateRequest.getAddress())
                .birthDate(employeeCreateRequest.getBirthDate())
                .hireDate(employeeCreateRequest.getHireDate())
                .department(department)
                .position(position)
                .status(EmployeeStatus.ACTIVE)
                .isDeleted(false)
                .build();
    }
}
