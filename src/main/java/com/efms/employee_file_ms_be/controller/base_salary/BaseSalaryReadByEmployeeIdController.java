package com.efms.employee_file_ms_be.controller.base_salary;

import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.base_salary.BaseSalaryReadByEmployeeIdCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/base-salaries")
@RequiredArgsConstructor
@Tag(name = "BaseSalary")
public class BaseSalaryReadByEmployeeIdController {

    private final BaseSalaryReadByEmployeeIdCmd readByEmployeeCommand;

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get base salary by employeeId")
    public ResponseEntity<BaseSalaryResponse> getByEmployee(@PathVariable String employeeId) {
        readByEmployeeCommand.setEmployeeId(employeeId);
        readByEmployeeCommand.execute();

        return ResponseEntity.ok(readByEmployeeCommand.getBaseSalaryResponse());
    }
}
