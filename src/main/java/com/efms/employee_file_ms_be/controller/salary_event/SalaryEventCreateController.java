package com.efms.employee_file_ms_be.controller.salary_event;

import com.efms.employee_file_ms_be.api.request.SalaryEventCreateRequest;
import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventCreateCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping("/api/salary-events")
@RequiredArgsConstructor
@Tag(name = "SalaryEvent")
public class SalaryEventCreateController {

    private final SalaryEventCreateCmd command;

    @PostMapping
    @Operation(summary = "Create a new Salary Event")
    public ResponseEntity<SalaryEventResponse> create(@Valid @RequestBody SalaryEventCreateRequest request) {
        command.setSalaryEventCreateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getSalaryEventResponse());
    }
}
