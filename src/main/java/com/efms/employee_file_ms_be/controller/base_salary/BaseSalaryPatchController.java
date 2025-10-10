package com.efms.employee_file_ms_be.controller.base_salary;

import com.efms.employee_file_ms_be.api.request.BaseSalaryUpdateRequest;
import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.base_salary.BaseSalaryPatchCmd;
import com.efms.employee_file_ms_be.controller.Constants;
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
@RequestMapping(Constants.Path.BASE_SALARY_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.BASE_SALARY)
public class BaseSalaryPatchController {

    private final BaseSalaryPatchCmd command;

    @PatchMapping("/{id}")
    @Operation(summary = "Update a Base Salary")
    public ResponseEntity<BaseSalaryResponse> patch(
            @PathVariable String id,
            @Valid @RequestBody BaseSalaryUpdateRequest request) {
        command.setId(id);
        command.setBaseSalaryUpdateRequest(request);
        command.execute();

        return ResponseEntity.ok(command.getBaseSalaryResponse());
    }
}
