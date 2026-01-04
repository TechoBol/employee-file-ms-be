package com.efms.employee_file_ms_be.api.request;

import com.efms.employee_file_ms_be.model.domain.SalaryEventCategory;
import com.efms.employee_file_ms_be.model.domain.SalaryEventFrequency;
import com.efms.employee_file_ms_be.model.domain.SalaryEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SalaryEventCreateRequest {

    @NotBlank
    private String employeeId;

    @NotNull
    private SalaryEventType type;

    private SalaryEventCategory category;

    private String description;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private SalaryEventFrequency frequency;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;
}
