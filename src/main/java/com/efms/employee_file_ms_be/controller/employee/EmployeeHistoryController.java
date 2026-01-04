package com.efms.employee_file_ms_be.controller.employee;

import com.efms.employee_file_ms_be.api.response.EmployeeHistoryResponse;
import com.efms.employee_file_ms_be.command.employee.EmployeeHistoryListCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.EMPLOYEE_PATH)
@RequiredArgsConstructor
@Tag(name = "Employee History", description = "Employee change history operations")
public class EmployeeHistoryController {

    private final EmployeeHistoryListCmd command;

    @GetMapping("/{employeeId}/history")
    @Operation(
            summary = "Get employee history with optional filters and pagination",
            description = "Retrieves the complete change history for a specific employee"
    )
    public ResponseEntity<Page<EmployeeHistoryResponse>> getEmployeeHistory(
            @PathVariable String employeeId,
            @RequestParam(required = false) String changeType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable
    ) {
        command.setEmployeeId(employeeId);
        command.setPage(pageable.getPageNumber());
        command.setSize(pageable.getPageSize());

        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                command.setSortBy(order.getProperty());
                command.setSortDirection(order.getDirection().name());
            });
        }

        command.execute();

        return ResponseEntity.ok(command.getHistoryPage());
    }
}