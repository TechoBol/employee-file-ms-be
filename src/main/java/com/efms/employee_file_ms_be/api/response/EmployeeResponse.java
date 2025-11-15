package com.efms.employee_file_ms_be.api.response;

import com.efms.employee_file_ms_be.model.domain.EmergencyContact;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class EmployeeResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String ci;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private String status;
    private EmergencyContact emergencyContact;
    private String departmentId;
    private String departmentName;
    private String positionId;
    private String positionName;
    private String type;
    private String branchId;
    private String branchName;
    private boolean isDisassociated;
    private LocalDateTime disassociatedAt;
}
