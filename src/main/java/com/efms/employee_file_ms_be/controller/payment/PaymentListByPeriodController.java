package com.efms.employee_file_ms_be.controller.payment;

import com.efms.employee_file_ms_be.api.request.EmployeeSearchRequest;
import com.efms.employee_file_ms_be.api.response.payment.PaymentSummaryResponse;
import com.efms.employee_file_ms_be.command.payment.PaymentListByPeriodCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import com.efms.employee_file_ms_be.model.domain.EmployeeType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String ci,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) EmployeeType type,
            @RequestParam(required = false) UUID branchId,
            @RequestParam(required = false) UUID positionId
    ) {
        EmployeeSearchRequest searchRequest = EmployeeSearchRequest.builder()
                .search(search)
                .ci(ci)
                .email(email)
                .phone(phone)
                .type(type)
                .branchId(branchId)
                .positionId(positionId)
                .build();

        command.setPeriod(period);
        command.setSearchRequest(searchRequest);
        command.execute();

        return ResponseEntity.ok(command.getPaymentSummary());
    }
}
