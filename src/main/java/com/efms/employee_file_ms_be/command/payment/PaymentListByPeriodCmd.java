package com.efms.employee_file_ms_be.command.payment;

import com.efms.employee_file_ms_be.api.request.EmployeeSearchRequest;
import com.efms.employee_file_ms_be.api.response.payment.PaymentEmployeeResponse;
import com.efms.employee_file_ms_be.api.response.payment.PaymentSummaryResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Payment;
import com.efms.employee_file_ms_be.model.mapper.PaymentMapper;
import com.efms.employee_file_ms_be.model.repository.PaymentRepository;
import com.efms.employee_file_ms_be.model.repository.specification.PaymentSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class PaymentListByPeriodCmd implements Command {

    @Setter
    private Integer period;

    @Setter
    private EmployeeSearchRequest searchRequest;

    @Getter
    private PaymentSummaryResponse paymentSummary;

    private final PaymentRepository repository;
    private final PaymentMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        List<Payment> payments;

        if (hasSearchFilters()) {
            Specification<Payment> spec = PaymentSpecification.withFilters(
                    searchRequest.getSearch(),
                    searchRequest.getCi(),
                    searchRequest.getEmail(),
                    searchRequest.getPhone(),
                    searchRequest.getType(),
                    searchRequest.getStatus(),
                    searchRequest.getIsDisassociated(),
                    searchRequest.getBranchId(),
                    searchRequest.getPositionId(),
                    period,
                    companyId
            );
            payments = repository.findAll(spec);
        } else {
            payments = repository.findAllByCompanyIdAndPeriod(companyId, period);
        }

        List<PaymentEmployeeResponse> paymentResponses = payments.stream()
                .map(this::toPaymentEmployeeResponse)
                .toList();

        paymentSummary = PaymentSummaryResponse.from(paymentResponses);
    }

    private PaymentEmployeeResponse toPaymentEmployeeResponse(Payment payment) {
        PaymentEmployeeResponse paymentEmployeeResponse = new PaymentEmployeeResponse();
        paymentEmployeeResponse.setEmployee(payment.getEmployeeDetails());
        paymentEmployeeResponse.setPayment(mapper.toDTO(payment));
        return paymentEmployeeResponse;
    }

    private boolean hasSearchFilters() {
        if (searchRequest == null) {
            return false;
        }

        return (searchRequest.getSearch() != null && !searchRequest.getSearch().isBlank())
                || (searchRequest.getCi() != null && !searchRequest.getCi().isBlank())
                || (searchRequest.getEmail() != null && !searchRequest.getEmail().isBlank())
                || (searchRequest.getPhone() != null && !searchRequest.getPhone().isBlank())
                || searchRequest.getType() != null
                || searchRequest.getStatus() != null
                || searchRequest.getIsDisassociated() != null
                || searchRequest.getBranchId() != null
                || searchRequest.getPositionId() != null;
    }
}
