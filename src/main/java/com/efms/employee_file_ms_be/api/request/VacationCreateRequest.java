package com.efms.employee_file_ms_be.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class VacationCreateRequest {

    @NotBlank
    private String employeeId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String notes;
}
