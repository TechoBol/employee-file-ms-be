package com.efms.employee_file_ms_be.model.domain;

import com.efms.employee_file_ms_be.api.response.payroll.PayrollDeductionResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
public class PaymentDetails {
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
    private List<PaymentDeduction> deductions;
    private BigDecimal totalDeductions;
    private BigDecimal netAmount;
}