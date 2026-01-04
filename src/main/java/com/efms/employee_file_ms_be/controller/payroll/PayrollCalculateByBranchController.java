package com.efms.employee_file_ms_be.controller.payroll;

import com.efms.employee_file_ms_be.api.response.payroll.PayrollByBranchResponse;
import com.efms.employee_file_ms_be.command.payroll.PayrollCalculateByBranchCmd;
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
@RequestMapping(Constants.Path.PAYROLL_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.PAYROLL)
public class PayrollCalculateByBranchController {

    private final PayrollCalculateByBranchCmd command;

    @GetMapping("/calculate/all/group-by-branch")
    public ResponseEntity<List<PayrollByBranchResponse>> getPayrollGroupedByBranch(
            @RequestParam(required = false) Integer period) {
        command.setPeriod(period);
        command.execute();

        return ResponseEntity.ok(command.getPayrollByBranch());
    }
}
