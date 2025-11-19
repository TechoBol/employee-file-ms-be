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

    private BigDecimal baseSalary; // Haber básico (salario del empleado)

    private Integer workedDays; // Días trabajados

    private Integer workingDaysPerMonth; // Días del mes (típicamente 30)

    private BigDecimal basicEarnings; // Sueldo básico (baseSalary * workedDays / workingDaysPerMonth)

    private Integer seniorityYears;

    private BigDecimal seniorityIncreasePercentage;

    private BigDecimal seniorityBonus; // Bono de antigüedad

    private BigDecimal otherBonuses; // Otros bonos (bonos manuales)

    private BigDecimal totalBonuses; // Total de bonos (antigüedad + otros)

    private BigDecimal totalEarnings; // Total ganado (basicEarnings + totalBonuses)

    private BigDecimal deductionAfpPercentage;

    private BigDecimal deductionAfp;

    private List<PayrollDeductionResponse> deductions;

    private BigDecimal totalDeductions;

    private BigDecimal netAmount; // Líquido pagable (totalEarnings - deductionAfp - totalDeductions)
}
