package com.efms.employee_file_ms_be.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseSalaryResponse {
    private String id;
    private String employeeId;
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
}