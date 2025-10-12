package com.efms.employee_file_ms_be.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeCreateRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String ci;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    private LocalDate birthDate;

    private LocalDate hireDate;

    @NotBlank
    private String departmentId;

    @NotBlank
    private String positionId;
}
