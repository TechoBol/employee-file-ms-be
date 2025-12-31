package com.efms.employee_file_ms_be.api.response.payment;

import com.efms.employee_file_ms_be.api.response.payroll.PayrollDeductionResponse;
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
public class PaymentSummaryResponse {
    private List<PaymentEmployeeResponse> payments;
    private PaymentTotals totals;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentTotals {
        private BigDecimal totalSeniorityBonuses;
        private BigDecimal totalOtherBonuses;
        private BigDecimal totalBonuses;
        private BigDecimal totalEarnings;
        private BigDecimal totalAfpDeductions;
        private BigDecimal totalDeductions;
        private BigDecimal netAmount;

        Map<String, BigDecimal> deductions = new HashMap<>();
    }

    public static PaymentSummaryResponse from(List<PaymentEmployeeResponse> payments) {
        PaymentSummaryResponse summary = new PaymentSummaryResponse();
        summary.setPayments(payments);

        PaymentTotals totals = new PaymentTotals();
        totals.setTotalBonuses(BigDecimal.ZERO);
        totals.setTotalEarnings(BigDecimal.ZERO);
        totals.setTotalDeductions(BigDecimal.ZERO);
        totals.setNetAmount(BigDecimal.ZERO);

        for (PaymentEmployeeResponse paymentEmployee : payments) {
            PaymentDetailsResponse payment = paymentEmployee.getPayment();
            if (payment != null) {
                totals.setTotalSeniorityBonuses(addNullSafe(totals.getTotalSeniorityBonuses(), payment.getSeniorityBonus()));
                totals.setTotalOtherBonuses(addNullSafe(totals.getTotalOtherBonuses(), payment.getOtherBonuses()));
                totals.setTotalBonuses(addNullSafe(totals.getTotalBonuses(), payment.getTotalBonuses()));
                totals.setTotalEarnings(addNullSafe(totals.getTotalEarnings(), payment.getTotalEarnings()));
                totals.setTotalAfpDeductions(addNullSafe(totals.getTotalAfpDeductions(), payment.getDeductionAfp()));
                totals.setTotalDeductions(addNullSafe(totals.getTotalDeductions(), payment.getTotalDeductions()));
                totals.setNetAmount(addNullSafe(totals.getNetAmount(), payment.getNetAmount()));

                if (payment.getDeductions() != null) {
                    for (PaymentDeductionResponse deduction : payment.getDeductions()) {
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
