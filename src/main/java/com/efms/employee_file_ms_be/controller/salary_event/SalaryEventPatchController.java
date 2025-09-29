package com.efms.employee_file_ms_be.controller.salary_event;

import com.efms.employee_file_ms_be.api.request.SalaryEventUpdateRequest;
import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventPatchCmd;
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
@RequestMapping("/api/salary-events")
@RequiredArgsConstructor
@Tag(name = "SalaryEvent")
public class SalaryEventPatchController {

    private final SalaryEventPatchCmd command;

    @PatchMapping("/{id}")
    @Operation(summary = "Update a Salary Event")
    public ResponseEntity<SalaryEventResponse> patch(
            @PathVariable String id,
            @Valid @RequestBody SalaryEventUpdateRequest request) {
        command.setId(id);
        command.setSalaryEventUpdateRequest(request);
        command.execute();

        return ResponseEntity.ok(command.getSalaryEventResponse());
    }
}
