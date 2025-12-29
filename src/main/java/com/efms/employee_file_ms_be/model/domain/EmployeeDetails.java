package com.efms.employee_file_ms_be.model.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class EmployeeDetails {
    private UUID id;
    private String firstName;
    private String lastName;
    private String ci;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private EmployeeStatus status;
    private EmployeeType type;
    private EmergencyContact emergencyContact;
    private Boolean isDeleted = Boolean.FALSE;
    private LocalDateTime deletedAt;
    private Boolean isDisassociated;
    private LocalDateTime disassociatedAt;
    private LocalDate disassociationDate;
    private String disassociationReason;
    private UUID branchId;
    private UUID positionId;
    private UUID companyId;
}
