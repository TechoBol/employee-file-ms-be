package com.efms.employee_file_ms_be.api.response.payment;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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
        private BigDecimal totalBonuses;
        private BigDecimal totalEarnings;
        private BigDecimal totalDeductions;
        private BigDecimal netAmount;
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
                totals.setTotalBonuses(addNullSafe(totals.getTotalBonuses(), payment.getTotalBonuses()));
                totals.setTotalEarnings(addNullSafe(totals.getTotalEarnings(), payment.getTotalEarnings()));
                totals.setTotalDeductions(addNullSafe(totals.getTotalDeductions(), payment.getTotalDeductions()));
                totals.setNetAmount(addNullSafe(totals.getNetAmount(), payment.getNetAmount()));
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
