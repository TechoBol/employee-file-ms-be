package com.efms.employee_file_ms_be.controller.payroll;

import com.efms.employee_file_ms_be.api.request.EmployeeSearchRequest;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollSummaryPageResponse;
import com.efms.employee_file_ms_be.command.payroll.PayrollCalculateByPageableCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import com.efms.employee_file_ms_be.model.domain.EmployeeType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.PAYROLL_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.PAYROLL)
public class PayrollCalculateByPageableController {

    private final PayrollCalculateByPageableCmd command;

    @GetMapping("/calculate")
    @Operation(summary = "Calculate payroll for employees with pagination")
    public ResponseEntity<PayrollSummaryPageResponse> calculateByPageable(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String ci,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) EmployeeType type,
            @RequestParam(required = false) UUID branchId,
            @RequestParam(required = false) UUID positionId,
            Pageable pageable
    ) {
        EmployeeSearchRequest searchRequest = EmployeeSearchRequest.builder()
                .search(search)
                .ci(ci)
                .email(email)
                .phone(phone)
                .type(type)
                .branchId(branchId)
                .positionId(positionId)
                .pageable(pageable)
                .build();

        command.setSearchRequest(searchRequest);
        command.setPageable(pageable);
        command.execute();

        return ResponseEntity.ok(command.getPayrollSummary());
    }
}
