package com.efms.employee_file_ms_be.model.mapper.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeCreateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.model.domain.*;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
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
                .departmentId(
                        Optional.ofNullable(employee.getPosition().getDepartment())
                                .map(d -> d.getId().toString())
                                .orElse(null)
                )
                .departmentName(
                        Optional.ofNullable(employee.getPosition().getDepartment())
                                .map(Department::getName)
                                .orElse(null)
                )
                .positionId(employee.getPosition().getId().toString())
                .positionName(employee.getPosition().getName())
                .branchId(employee.getBranch().getId().toString())
                .branchName(employee.getBranch().getName())
                .build();
    }

    @Override
    public Employee toEntity(EmployeeCreateRequest employeeCreateRequest) {
        Position position = new Position();
        Branch branch = new Branch();
        position.setId(UUID.fromString(employeeCreateRequest.getPositionId()));
        branch.setId(UUID.fromString(employeeCreateRequest.getBranchId()));
        return Employee.builder()
                .firstName(employeeCreateRequest.getFirstName())
                .lastName(employeeCreateRequest.getLastName())
                .ci(employeeCreateRequest.getCi())
                .email(employeeCreateRequest.getEmail())
                .phone(employeeCreateRequest.getPhone())
                .address(employeeCreateRequest.getAddress())
                .birthDate(employeeCreateRequest.getBirthDate())
                .hireDate(employeeCreateRequest.getHireDate())
                .position(position)
                .branch(branch)
                .status(EmployeeStatus.ACTIVE)
                .isDeleted(false)
                .build();
    }
}
