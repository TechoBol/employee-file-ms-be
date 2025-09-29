package com.efms.employee_file_ms_be.controller.base_salary;

import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.base_salary.BaseSalaryReadByIdCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
public class BaseSalaryReadByIdController {

    private final BaseSalaryReadByIdCmd command;

    @GetMapping("/{id}")
    @Operation(summary = "Get base salary by ID")
    public ResponseEntity<BaseSalaryResponse> getById(@PathVariable String id) {
        command.setId(id);
        command.execute();

        return ResponseEntity.ok(command.getBaseSalaryResponse());
    }
}
