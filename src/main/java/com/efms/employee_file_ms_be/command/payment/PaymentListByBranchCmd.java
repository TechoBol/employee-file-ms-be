package com.efms.employee_file_ms_be.command.payment;

import com.efms.employee_file_ms_be.api.response.payment.*;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.EmployeeDetails;
import com.efms.employee_file_ms_be.model.domain.Payment;
import com.efms.employee_file_ms_be.model.mapper.PaymentMapper;
import com.efms.employee_file_ms_be.model.repository.PaymentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class PaymentListByBranchCmd implements Command {

    @Setter
    private Integer period;

    @Getter
    private List<PaymentByBranchResponse> paymentByBranch;

    private final PaymentRepository repository;
    private final PaymentMapper mapper;

    private static final UUID NO_BRANCH = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        List<Payment> payments = repository.findAllByCompanyIdAndPeriod(companyId, period);

        Map<UUID, List<Payment>> paymentsByBranch = payments.stream()
                .collect(Collectors.groupingBy(
                        payment -> {
                            EmployeeDetails employee = payment.getEmployeeDetails();
                            return employee != null && employee.getBranchId() != null
                                    ? employee.getBranchId()
                                    : NO_BRANCH;
                        }
                ));

        paymentByBranch = paymentsByBranch.entrySet().stream()
                .map(entry -> {
                    UUID branchId = entry.getKey();
                    List<Payment> branchPayments = entry.getValue();

                    List<PaymentEmployeeResponse> paymentResponses = branchPayments.stream()
                            .map(this::toPaymentEmployeeResponse)
                            .toList();

                    PaymentByBranchResponse branchResponse = new PaymentByBranchResponse();

                    branchResponse.setBranchId(branchId.equals(NO_BRANCH) ? null : branchId);

                    if (!branchPayments.isEmpty()
                            && branchPayments.getFirst().getEmployeeDetails() != null
                            && branchPayments.getFirst().getEmployeeDetails().getBranchName() != null
                            && !branchId.equals(NO_BRANCH)) {
                        branchResponse.setBranchName(
                                branchPayments.getFirst().getEmployeeDetails().getBranchName()
                        );
                    } else {
                        branchResponse.setBranchName("Sin Sucursal");
                    }

                    branchResponse.setEmployeeCount(branchPayments.size());
                    branchResponse.setPayments(paymentResponses);
                    branchResponse.setTotals(calculateBranchTotals(paymentResponses));

                    return branchResponse;
                })
                .sorted(Comparator.comparing(
                        PaymentByBranchResponse::getBranchName,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .toList();
    }

    private PaymentEmployeeResponse toPaymentEmployeeResponse(Payment payment) {
        PaymentEmployeeResponse paymentEmployeeResponse = new PaymentEmployeeResponse();
        paymentEmployeeResponse.setEmployee(payment.getEmployeeDetails());
        paymentEmployeeResponse.setPayment(mapper.toDTO(payment));
        return paymentEmployeeResponse;
    }

    private PaymentSummaryResponse.PaymentTotals calculateBranchTotals(List<PaymentEmployeeResponse> payments) {
        PaymentSummaryResponse.PaymentTotals totals = new PaymentSummaryResponse.PaymentTotals();
        totals.setDeductions(new HashMap<>());

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
                        totals.getDeductions().put(type,
                                addNullSafe(totals.getDeductions().get(type), amount));
                    }
                }
            }
        }

        return totals;
    }

    private static BigDecimal addNullSafe(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a)
                .add(b == null ? BigDecimal.ZERO : b);
    }
}
