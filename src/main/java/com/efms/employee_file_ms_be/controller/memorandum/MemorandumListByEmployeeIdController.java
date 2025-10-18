package com.efms.employee_file_ms_be.controller.memorandum;

import com.efms.employee_file_ms_be.api.response.MemorandumResponse;
import com.efms.employee_file_ms_be.command.memorandum.MemorandumListByEmployeeIdCmd;
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
@RequestMapping(Constants.Path.MEMORANDUM_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.MEMORANDUM)
public class MemorandumListByEmployeeIdController {

    private final MemorandumListByEmployeeIdCmd command;

    @GetMapping("/employees/{employeeId}")
    @Operation(summary = "Get memorandums by employee ID")
    public ResponseEntity<List<MemorandumResponse>> getMemorandumsByEmployeeId(
            @PathVariable String employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        command.setStartDate(startDate);
        command.setEndDate(endDate);
        command.setEmployeeId(employeeId);
        command.execute();

        return ResponseEntity.ok(command.getMemorandumResponseList());
    }
}
