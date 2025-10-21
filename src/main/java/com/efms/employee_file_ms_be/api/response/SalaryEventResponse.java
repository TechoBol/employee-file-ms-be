package com.efms.employee_file_ms_be.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SalaryEventResponse {

    private String id;

    private String employeeId;

    private String type;

    private String category;

    private String description;

    private BigDecimal amount;

    private String frequency;

    private LocalDate startDate;

    private LocalDate endDate;
}
