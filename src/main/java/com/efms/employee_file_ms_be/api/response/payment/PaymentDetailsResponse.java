package com.efms.employee_file_ms_be.api.response.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
@Builder
public class PaymentDetailsResponse {

    private Integer period;

    private BigDecimal baseSalary;

    private Integer workedDays;

    private BigDecimal basicEarnings;

    private Integer seniorityYears;

    private BigDecimal seniorityIncreasePercentage;

    private BigDecimal seniorityBonus;

    private BigDecimal grossAmount;

    private BigDecimal deductionAfpPercentage;

    private BigDecimal deductionAfp;

    private List<PaymentDeductionResponse> deductions;

    private BigDecimal totalDeduction;

    private BigDecimal netAmount;
}
