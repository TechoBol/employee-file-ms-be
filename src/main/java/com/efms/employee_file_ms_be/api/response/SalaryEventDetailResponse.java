package com.efms.employee_file_ms_be.api.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Josue Veliz
 */
public class SalaryEventDetailResponse {

    private String id;

    private EmployeeResponse employee;

    private String type;

    private String description;

    private BigDecimal amount;

    private String frequency;

    private LocalDate startDate;

    private LocalDate endDate;
}
