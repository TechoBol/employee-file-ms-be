package com.efms.employee_file_ms_be.controller.payment;

import com.efms.employee_file_ms_be.api.response.payment.PaymentDetailsResponse;
import com.efms.employee_file_ms_be.command.payment.PaymentListByEmployeeAndPeriodCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.PAYMENT_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.PAYMENT)
public class PaymentListByEmployeeAndPeriodController {

    private final PaymentListByEmployeeAndPeriodCmd command;

    @GetMapping("/employees/{employeeId}/periods/{period}")
    @Operation(summary = "List payments for an employee in a specific period")
    public ResponseEntity<List<PaymentDetailsResponse>> listByEmployeeAndPeriod(
            @PathVariable String employeeId,
            @PathVariable Integer period
    ) {
        command.setEmployeeId(employeeId);
        command.setPeriod(period);
        command.execute();

        return ResponseEntity.ok(command.getPaymentResponseList());
    }
}
