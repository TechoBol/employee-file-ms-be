package com.efms.employee_file_ms_be.controller.payment;

import com.efms.employee_file_ms_be.api.response.payment.PaymentByBranchResponse;
import com.efms.employee_file_ms_be.command.payment.PaymentListByBranchCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.PAYMENT_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.PAYMENT)
public class PaymentListByBranchController {

    private final PaymentListByBranchCmd command;

    @GetMapping("/group-by-branch")
    public ResponseEntity<List<PaymentByBranchResponse>> getPaymentGroupedByBranch(
            @RequestParam(required = false) Integer period) {
        command.setPeriod(period);
        command.execute();

        return ResponseEntity.ok(command.getPaymentByBranch());
    }
}
