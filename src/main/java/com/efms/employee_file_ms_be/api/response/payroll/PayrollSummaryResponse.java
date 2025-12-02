package com.efms.employee_file_ms_be.api.response.payroll;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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
        private BigDecimal totalBonuses;
        private BigDecimal totalEarnings;
        private BigDecimal totalDeductions;
        private BigDecimal netAmount;
    }

    public static PayrollSummaryResponse from(List<PayrollEmployeeResponse> payrolls) {
        PayrollSummaryResponse summary = new PayrollSummaryResponse();
        summary.setPayrolls(payrolls);

        PayrollTotals totals = new PayrollTotals();

        for (PayrollEmployeeResponse payrollEmployee : payrolls) {
            PayrollResponse payroll = payrollEmployee.getPayroll();
            if (payroll != null) {
                totals.setTotalBonuses(addNullSafe(totals.getTotalBonuses(), payroll.getTotalBonuses()));
                totals.setTotalEarnings(addNullSafe(totals.getTotalEarnings(), payroll.getTotalEarnings()));
                totals.setTotalDeductions(addNullSafe(totals.getTotalDeductions(), payroll.getTotalDeductions()));
                totals.setNetAmount(addNullSafe(totals.getNetAmount(), payroll.getNetAmount()));
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
