package com.efms.employee_file_ms_be.controller.payment;

import com.efms.employee_file_ms_be.api.response.payment.PaymentSummaryResponse;
import com.efms.employee_file_ms_be.command.payment.PaymentListByPeriodCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.PAYMENT_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.PAYMENT)
public class PaymentListByPeriodController {

    private final PaymentListByPeriodCmd command;

    @GetMapping("/periods/{period}/all")
    @Operation(summary = "List payments for all employees in a specific period")
    public ResponseEntity<PaymentSummaryResponse> listByPeriod(
            @PathVariable Integer period,
            Pageable page
    ) {
        command.setPeriod(period);
        command.execute();

        return ResponseEntity.ok(command.getPaymentSummary());
    }
}
