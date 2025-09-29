package com.efms.employee_file_ms_be.controller.employee;

import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.employee.EmployeeReadByPageableCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee")
public class EmployeeReadByPageableController {

    private final EmployeeReadByPageableCmd command;

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get employees by company with pagination")
    public ResponseEntity<Page<EmployeeResponse>> getByCompanyPageable(
            @PathVariable String companyId,
            Pageable pageable) {
        command.setCompanyId(companyId);
        command.setPageable(pageable);
        command.execute();

        return ResponseEntity.ok(command.getEmployees());
    }
}
