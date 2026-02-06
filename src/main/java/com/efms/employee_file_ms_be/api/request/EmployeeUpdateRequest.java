package com.efms.employee_file_ms_be.api.request;

import com.efms.employee_file_ms_be.model.domain.EmergencyContact;
import com.efms.employee_file_ms_be.model.domain.EmployeeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeUpdateRequest {

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

    private EmployeeType type;

    private String positionId;

    private String branchId;

    private LocalDate disassociationDate;

    private String disassociationReason;

    private String contractCompany;

    private String contractPosition;
}
