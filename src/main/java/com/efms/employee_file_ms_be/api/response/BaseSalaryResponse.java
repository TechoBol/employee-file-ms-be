package com.efms.employee_file_ms_be.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseSalaryResponse {
    private String id;
    private String employeeId;
    private String employeeFirstName;
    private String employeeLastName;
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
}