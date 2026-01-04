package com.efms.employee_file_ms_be.model.mapper;

import com.efms.employee_file_ms_be.api.response.payment.PaymentDeductionResponse;
import com.efms.employee_file_ms_be.api.response.payment.PaymentDetailsResponse;
import com.efms.employee_file_ms_be.model.domain.Payment;
import com.efms.employee_file_ms_be.model.domain.PaymentDeduction;
import com.efms.employee_file_ms_be.model.domain.PaymentDetails;
import org.springframework.stereotype.Component;

/**
 * @author Josue Veliz
 */
@Component
public class PaymentMapper {

    public PaymentDetailsResponse toDTO(Payment payment) {
        PaymentDetails paymentDetails = payment.getPaymentDetails();
        return PaymentDetailsResponse.builder()
                .period(payment.getPeriod())
                .baseSalary(paymentDetails.getBaseSalary())
                .workedDays(paymentDetails.getWorkedDays())
                .workingDaysPerMonth(paymentDetails.getWorkingDaysPerMonth())
                .basicEarnings(paymentDetails.getBasicEarnings())
                .seniorityYears(paymentDetails.getSeniorityYears())
                .seniorityIncreasePercentage(paymentDetails.getSeniorityIncreasePercentage())
                .seniorityBonus(paymentDetails.getSeniorityBonus())
                .otherBonuses(paymentDetails.getOtherBonuses())
                .totalBonuses(paymentDetails.getTotalBonuses())
                .totalEarnings(paymentDetails.getTotalEarnings())
                .deductionAfpPercentage(paymentDetails.getDeductionAfpPercentage())
                .deductionAfp(paymentDetails.getDeductionAfp())
                .deductions(paymentDetails.getDeductions().stream()
                        .map(this::mapDeduction)
                        .toList())
                .totalDeductions(paymentDetails.getTotalDeductions())
                .netAmount(paymentDetails.getNetAmount())
                .build();
    }

    private PaymentDeductionResponse mapDeduction(PaymentDeduction deduction) {
        return PaymentDeductionResponse.builder()
                .type(deduction.getType())
                .qty(deduction.getQty())
                .totalDeduction(deduction.getTotalDeduction())
                .build();
    }
}
