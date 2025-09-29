package com.efms.employee_file_ms_be.controller.base_salary;

import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.base_salary.BaseSalaryReadByPageableCmd;
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
@RequestMapping("/api/base-salaries")
@RequiredArgsConstructor
@Tag(name = "BaseSalary")
public class BaseSalaryReadByPageableController {

    private final BaseSalaryReadByPageableCmd command;

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get base salaries by company with pagination")
    public ResponseEntity<Page<BaseSalaryResponse>> getByCompanyPageable(
            @PathVariable String companyId,
            Pageable pageable) {
        command.setCompanyId(companyId);
        command.setPageable(pageable);
        command.execute();

        return ResponseEntity.ok(command.getBaseSalaryResponse());
    }
}
