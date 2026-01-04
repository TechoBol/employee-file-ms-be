package com.efms.employee_file_ms_be.controller.payroll;

import com.efms.employee_file_ms_be.api.response.payroll.PayrollSummaryResponse;
import com.efms.employee_file_ms_be.command.payroll.PayrollCalculateAllCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.PAYROLL_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.PAYROLL)
public class PayrollCalculateAllController {

    private final PayrollCalculateAllCmd command;

    @GetMapping("/calculate/all")
    @Operation(summary = "Calculate payroll for employees")
    public ResponseEntity<PayrollSummaryResponse> calculateAll() {
        command.execute();

        return ResponseEntity.ok(command.getPayrollSummary());
    }
}
