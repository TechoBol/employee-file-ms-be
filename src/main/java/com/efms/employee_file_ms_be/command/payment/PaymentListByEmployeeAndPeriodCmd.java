package com.efms.employee_file_ms_be.command.payment;

import com.efms.employee_file_ms_be.api.response.payment.PaymentDetailsResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Payment;
import com.efms.employee_file_ms_be.model.mapper.payment.PaymentMapper;
import com.efms.employee_file_ms_be.model.repository.PaymentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class PaymentListByEmployeeAndPeriodCmd implements Command {

    @Setter
    private String employeeId;

    @Setter
    private Integer period;

    @Getter
    private List<Payment> paymentList;

    @Getter
    private List<PaymentDetailsResponse> paymentResponseList;

    private final PaymentRepository repository;

    private final PaymentMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        paymentList = repository.findAllByCompanyIdAndEmployeeIdAndPeriod(
                companyId,
                UUID.fromString(employeeId),
                period
        );
        paymentResponseList = paymentList.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
