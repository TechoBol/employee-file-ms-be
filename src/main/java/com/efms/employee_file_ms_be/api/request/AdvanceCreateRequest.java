package com.efms.employee_file_ms_be.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class AdvanceCreateRequest {
    @NotBlank
    private String employeeId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private LocalDate advanceDate;
}
