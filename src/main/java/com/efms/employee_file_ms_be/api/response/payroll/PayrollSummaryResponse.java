package com.efms.employee_file_ms_be.api.response.payroll;

import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class PayrollSummaryResponse {
    private List<PayrollEmployeeResponse> payrolls;
    private PayrollTotals totals;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayrollTotals {
        private BigDecimal totalSeniorityBonuses;
        private BigDecimal totalOtherBonuses;
        private BigDecimal totalBonuses;
        private BigDecimal totalEarnings;
        private BigDecimal totalAfpDeductions;
        private BigDecimal totalDeductions;
        private BigDecimal netAmount;

        Map<String, BigDecimal> deductions = new HashMap<>();
    }

    public static PayrollSummaryResponse from(List<PayrollEmployeeResponse> payrolls) {
        PayrollSummaryResponse summary = new PayrollSummaryResponse();
        summary.setPayrolls(payrolls);

        PayrollTotals totals = new PayrollTotals();

        for (PayrollEmployeeResponse payrollEmployee : payrolls) {
            PayrollResponse payroll = payrollEmployee.getPayroll();
            if (payroll != null) {
                totals.setTotalSeniorityBonuses(addNullSafe(totals.getTotalSeniorityBonuses(), payroll.getSeniorityBonus()));
                totals.setTotalOtherBonuses(addNullSafe(totals.getTotalOtherBonuses(), payroll.getOtherBonuses()));
                totals.setTotalBonuses(addNullSafe(totals.getTotalBonuses(), payroll.getTotalBonuses()));
                totals.setTotalEarnings(addNullSafe(totals.getTotalEarnings(), payroll.getTotalEarnings()));
                totals.setTotalAfpDeductions(addNullSafe(totals.getTotalAfpDeductions(), payroll.getDeductionAfp()));
                totals.setTotalDeductions(addNullSafe(totals.getTotalDeductions(), payroll.getTotalDeductions()));
                totals.setNetAmount(addNullSafe(totals.getNetAmount(), payroll.getNetAmount()));

                if (payroll.getDeductions() != null) {
                    for (PayrollDeductionResponse deduction : payroll.getDeductions()) {
                        String type = deduction.getType();
                        BigDecimal amount = deduction.getTotalDeduction();
                        totals.deductions.put(type,
                                addNullSafe(totals.deductions.get(type), amount));
                    }
                }
            }
        }

        summary.setTotals(totals);
        return summary;
    }

    private static BigDecimal addNullSafe(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a)
                .add(b == null ? BigDecimal.ZERO : b);
    }
}
