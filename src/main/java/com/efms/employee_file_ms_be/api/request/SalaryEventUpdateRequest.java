package com.efms.employee_file_ms_be.api.request;

import com.efms.employee_file_ms_be.model.domain.SalaryEventType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class SalaryEventUpdateRequest {

    private SalaryEventType type;

    private String description;

    private BigDecimal amount;

    private String frequency;

    private LocalDate startDate;

    private LocalDate endDate;
}
