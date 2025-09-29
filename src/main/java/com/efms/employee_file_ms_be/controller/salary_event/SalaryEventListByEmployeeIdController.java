package com.efms.employee_file_ms_be.controller.salary_event;

import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventListByEmployeeIdCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping("/api/salary-events")
@RequiredArgsConstructor
@Tag(name = "SalaryEvent")
public class SalaryEventListByEmployeeIdController {

    private final SalaryEventListByEmployeeIdCmd command;

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get salary events by employee ID")
    public ResponseEntity<List<SalaryEventResponse>> getByEmployee(@PathVariable String employeeId) {
        command.setEmployeeId(employeeId);
        command.execute();

        return ResponseEntity.ok(command.getSalaryEventResponseList());
    }
}
