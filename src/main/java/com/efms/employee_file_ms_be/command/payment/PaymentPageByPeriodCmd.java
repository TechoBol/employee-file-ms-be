package com.efms.employee_file_ms_be.command.payment;

import com.efms.employee_file_ms_be.api.response.payment.PaymentEmployeeResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Payment;
import com.efms.employee_file_ms_be.model.mapper.employee.EmployeeMapper;
import com.efms.employee_file_ms_be.model.mapper.payment.PaymentMapper;
import com.efms.employee_file_ms_be.model.repository.PaymentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class PaymentPageByPeriodCmd implements Command {

    @Setter
    private Integer period;

    @Setter
    private Pageable pageable;

    @Getter
    private Page<Payment> paymentPage;

    @Getter
    private Page<PaymentEmployeeResponse> paymentResponsePage;

    private final PaymentRepository repository;

    private final PaymentMapper mapper;

    private final EmployeeMapper employeeMapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        paymentPage = repository.findAllByCompanyIdAndPeriod(
                companyId,
                period,
                pageable
        );

        paymentResponsePage = paymentPage.map(this::toPaymentEmployeeResponse);
    }

    private PaymentEmployeeResponse toPaymentEmployeeResponse(Payment payment) {
        PaymentEmployeeResponse paymentEmployeeResponse = new PaymentEmployeeResponse();
        paymentEmployeeResponse.setEmployee(employeeMapper.toDTO(payment.getEmployee()));
        paymentEmployeeResponse.setPayment(mapper.toDTO(payment));
        return paymentEmployeeResponse;
    }
}