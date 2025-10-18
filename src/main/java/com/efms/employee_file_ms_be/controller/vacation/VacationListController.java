package com.efms.employee_file_ms_be.controller.vacation;

import com.efms.employee_file_ms_be.api.response.VacationResponse;
import com.efms.employee_file_ms_be.command.vacation.VacationListCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.VACATION_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.VACATION)
public class VacationListController {

    private final VacationListCmd command;

    @GetMapping
    @Operation(summary = "Get vacations")
    public ResponseEntity<List<VacationResponse>> getVacationsByCompanyId(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        command.setStartDate(startDate);
        command.setEndDate(endDate);
        command.execute();

        return ResponseEntity.ok(command.getVacationResponseList());
    }
}
