package com.efms.employee_file_ms_be.controller.department;

import com.efms.employee_file_ms_be.api.request.DepartmentUpdateRequest;
import com.efms.employee_file_ms_be.api.response.DepartmentResponse;
import com.efms.employee_file_ms_be.command.department.DepartmentPatchCmd;
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
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Department")
public class DepartmentPatchController {

    private final DepartmentPatchCmd command;

    @PatchMapping("/{id}")
    @Operation(summary = "Update a Department")
    public ResponseEntity<DepartmentResponse> patch(
            @PathVariable String id,
            @Valid @RequestBody DepartmentUpdateRequest request) {
        command.setId(id);
        command.setDepartmentUpdateRequest(request);
        command.execute();

        return ResponseEntity.ok(command.getDepartmentResponse());
    }
}
