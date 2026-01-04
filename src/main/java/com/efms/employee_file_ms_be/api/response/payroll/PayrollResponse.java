package com.efms.employee_file_ms_be.api.response.payroll;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class PayrollResponse {
    private BigDecimal baseSalary;
    private Integer workedDays;
    private Integer workingDaysPerMonth;
    private BigDecimal basicEarnings;
    private Integer seniorityYears;
    private BigDecimal seniorityIncreasePercentage;
    private BigDecimal seniorityBonus;
    private BigDecimal otherBonuses;
    private BigDecimal totalBonuses;
    private BigDecimal totalEarnings;
    private BigDecimal deductionAfpPercentage;
    private BigDecimal deductionAfp;
    private List<PayrollDeductionResponse> deductions;
    private BigDecimal totalDeductions;
    private BigDecimal netAmount;
}
