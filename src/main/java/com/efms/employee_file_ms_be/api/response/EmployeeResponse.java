package com.efms.employee_file_ms_be.api.response;

import com.efms.employee_file_ms_be.model.domain.EmployeeStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class EmployeeResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private String status;
    private String departmentId;
    private String departmentName;
    private String positionId;
    private String positionName;
    private String locationId;
    private String locationName;
    private String companyId;
}
