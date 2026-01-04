package com.efms.employee_file_ms_be.controller.memorandum;

import com.efms.employee_file_ms_be.api.response.MemorandumResponse;
import com.efms.employee_file_ms_be.command.memorandum.MemorandumListCmd;
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
@RequestMapping(Constants.Path.MEMORANDUM_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.MEMORANDUM)
public class MemorandumListController {

    private final MemorandumListCmd command;

    @GetMapping
    @Operation(summary = "Get memorandums")
    public ResponseEntity<List<MemorandumResponse>> getMemorandumsByCompanyId(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        command.setStartDate(startDate);
        command.setEndDate(endDate);
        command.execute();

        return ResponseEntity.ok(command.getMemorandumResponseList());
    }
}
