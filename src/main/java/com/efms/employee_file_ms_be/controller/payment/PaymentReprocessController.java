package com.efms.employee_file_ms_be.controller.payment;

import com.efms.employee_file_ms_be.api.response.payment.PaymentReprocessSummary;
import com.efms.employee_file_ms_be.command.payment.PaymentReprocessCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.Path.PAYMENT_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.PAYMENT)
public class PaymentReprocessController {

    private final PaymentReprocessCmd cmd;

    @PostMapping("/periods/{period}")
    @Operation(summary = "Reprocess payments for all employees in a specific period with pagination")
    public ResponseEntity<PaymentReprocessSummary> listByPeriod(
            @PathVariable Integer period
    ) {
        cmd.setPeriod(period);
        cmd.execute();

        return ResponseEntity.ok(cmd.getSummary());
    }
}
