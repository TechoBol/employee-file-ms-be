package com.efms.employee_file_ms_be.api.response.payroll;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
public class PayrollDeductionResponse {
    private String type;
    private Integer qty;
    private BigDecimal totalDeduction;
}
