package com.efms.employee_file_ms_be.controller.absence;

import com.efms.employee_file_ms_be.api.response.AbsenceResponse;
import com.efms.employee_file_ms_be.command.absence.AbsenceListByEmployeeIdCmd;
import com.efms.employee_file_ms_be.controller.Constants;
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
@RequestMapping(Constants.Path.ABSENCE_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.ABSENCE)
public class AbsenceListByEmployeeIdController {

    private final AbsenceListByEmployeeIdCmd command;

    @GetMapping("/employees/{employeeId}")
    @Operation(summary = "Get absences by employee ID")
    public ResponseEntity<List<AbsenceResponse>> getAbsencesByEmployeeId(
            @PathVariable String employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Boolean useActualDate) {
        command.setStartDate(startDate);
        command.setEndDate(endDate);
        command.setEmployeeId(employeeId);
        command.setUseActualDate(useActualDate);
        command.execute();

        return ResponseEntity.ok(command.getAbsenceResponseList());
    }
}
