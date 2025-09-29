package com.efms.employee_file_ms_be.controller.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeUpdateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.employee.EmployeePatchCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee")
public class EmployeePatchController {

    private final EmployeePatchCmd command;

    @PatchMapping("/{id}")
    @Operation(summary = "Update an Employee")
    public ResponseEntity<EmployeeResponse> patch(
            @PathVariable String id,
            @Valid @RequestBody EmployeeUpdateRequest request) {
        command.setId(id);
        command.setEmployeeUpdateRequest(request);
        command.execute();

        return ResponseEntity.ok(command.getEmployeeResponse());
    }
}
