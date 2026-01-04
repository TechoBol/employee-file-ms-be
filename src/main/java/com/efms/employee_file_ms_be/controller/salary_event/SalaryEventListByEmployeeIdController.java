package com.efms.employee_file_ms_be.controller.salary_event;

import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventListByEmployeeIdCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import com.efms.employee_file_ms_be.model.domain.SalaryEventCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.SALARY_EVENT_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.SALARY_EVENT)
public class SalaryEventListByEmployeeIdController {

    private final SalaryEventListByEmployeeIdCmd command;

    @GetMapping("/employees/{employeeId}")
    @Operation(summary = "Get salary events by employee ID")
    public ResponseEntity<List<SalaryEventResponse>> getByEmployee(
            @PathVariable String employeeId,
            @RequestParam(required = false) SalaryEventCategory category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Boolean useActualDate) {
        command.setCategory(category);
        command.setStartDate(startDate);
        command.setEndDate(endDate);
        command.setEmployeeId(employeeId);
        command.setUseActualDate(useActualDate);
        command.execute();

        return ResponseEntity.ok(command.getSalaryEventResponseList());
    }
}
