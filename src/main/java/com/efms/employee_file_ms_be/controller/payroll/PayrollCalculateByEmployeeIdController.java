package com.efms.employee_file_ms_be.controller.payroll;

import com.efms.employee_file_ms_be.api.response.payroll.PayrollResponse;
import com.efms.employee_file_ms_be.command.payroll.PayrollCalculateByEmployeeIdCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.PAYROLL_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.PAYROLL)
public class PayrollCalculateByEmployeeIdController {

    private final PayrollCalculateByEmployeeIdCmd command;

    @GetMapping("/calculate/employees/{employeeId}")
    @Operation(summary = "Calculate payroll for an employee")
    public ResponseEntity<PayrollResponse> calculateByEmployee(@PathVariable String employeeId) {
        command.setEmployeeId(employeeId);
        command.execute();

        return ResponseEntity.ok(command.getPayrollResponse());
    }
}
