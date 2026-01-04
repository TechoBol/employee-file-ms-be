package com.efms.employee_file_ms_be.model.mapper;

import com.efms.employee_file_ms_be.api.request.EmployeeCreateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.model.domain.*;
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
                .emergencyContact(employee.getEmergencyContact())
                .type(employee.getType().name())
                .departmentId(
                        Optional.ofNullable(employee.getPosition())
                                .map(Position::getDepartment)
                                .map(d -> d.getId().toString())
                                .orElse(null)
                )
                .departmentName(
                        Optional.ofNullable(employee.getPosition())
                                .map(Position::getDepartment)
                                .map(Department::getName)
                                .orElse(null)
                )
                .positionId(
                        Optional.ofNullable(employee.getPosition())
                                .map(p -> p.getId().toString())
                                .orElse(null)
                )
                .positionName(
                        Optional.ofNullable(employee.getPosition())
                                .map(Position::getName)
                                .orElse(null)
                )
                .branchId(
                        Optional.ofNullable(employee.getBranch())
                                .map(b -> b.getId().toString())
                                .orElse(null)
                )
                .branchName(
                        Optional.ofNullable(employee.getBranch())
                                .map(Branch::getName)
                                .orElse(null)
                )
                .isDisassociated(employee.getIsDisassociated())
                .disassociatedAt(employee.getDisassociatedAt())
                .disassociationDate(employee.getDisassociationDate())
                .disassociationReason(employee.getDisassociationReason())
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
                .type(employeeCreateRequest.getType())
                .position(position)
                .branch(branch)
                .status(EmployeeStatus.ACTIVE)
                .isDeleted(false)
                .isDisassociated(false)
                .build();
    }
}
