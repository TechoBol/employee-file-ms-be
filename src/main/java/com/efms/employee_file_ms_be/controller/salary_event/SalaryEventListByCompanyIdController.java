package com.efms.employee_file_ms_be.controller.salary_event;

import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventListByCompanyIdCmd;
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
public class SalaryEventListByCompanyIdController {

    private final SalaryEventListByCompanyIdCmd command;

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get salary events by company ID")
    public ResponseEntity<List<SalaryEventResponse>> getByCompany(@PathVariable String companyId) {
        command.setCompanyId(companyId);
        command.execute();

        return ResponseEntity.ok(command.getSalaryEventResponseList());
    }
}
