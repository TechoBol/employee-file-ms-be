package com.efms.employee_file_ms_be.controller.payment;

import com.efms.employee_file_ms_be.api.response.payment.PaymentEmployeeResponse;
import com.efms.employee_file_ms_be.command.payment.PaymentPageByPeriodCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.PAYMENT_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.PAYMENT)
public class PaymentPageByPeriodController {

    private final PaymentPageByPeriodCmd command;

    @GetMapping("/periods/{period}")
    @Operation(summary = "List payments for all employees in a specific period with pagination")
    public ResponseEntity<Page<PaymentEmployeeResponse>> listByPeriod(
            @PathVariable Integer period,
            Pageable page
    ) {
        command.setPeriod(period);
        command.setPageable(page);
        command.execute();

        return ResponseEntity.ok(command.getPaymentResponsePage());
    }
}
