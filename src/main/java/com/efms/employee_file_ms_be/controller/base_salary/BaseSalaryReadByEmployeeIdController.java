package com.efms.employee_file_ms_be.controller.base_salary;

import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.base_salary.BaseSalaryReadByEmployeeIdCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.Path.BASE_SALARY_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.BASE_SALARY)
public class BaseSalaryReadByEmployeeIdController {

    private final BaseSalaryReadByEmployeeIdCmd readByEmployeeCommand;

    @GetMapping("/employees/{employeeId}")
    @Operation(summary = "Get base salary by employeeId")
    public ResponseEntity<BaseSalaryResponse> getByEmployee(@PathVariable String employeeId) {
        readByEmployeeCommand.setEmployeeId(employeeId);
        readByEmployeeCommand.execute();

        return ResponseEntity.ok(readByEmployeeCommand.getBaseSalaryResponse());
    }
}
