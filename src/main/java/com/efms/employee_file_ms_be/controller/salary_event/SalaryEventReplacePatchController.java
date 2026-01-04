package com.efms.employee_file_ms_be.controller.salary_event;

import com.efms.employee_file_ms_be.api.request.SalaryEventUpdateRequest;
import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventReplacePatchCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.SALARY_EVENT_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.SALARY_EVENT)
public class SalaryEventReplacePatchController {

    private final SalaryEventReplacePatchCmd command;

    @PatchMapping("/{id}/replace")
    @Operation(summary = "Replace a SalaryEvent by deleting and recreating it with updated data")
    public ResponseEntity<SalaryEventResponse> replaceSalaryEvent(@PathVariable String id,
                                                                  @Valid @RequestBody SalaryEventUpdateRequest request) {
        command.setId(id);
        command.setSalaryEventUpdateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.OK).body(command.getSalaryEventResponse());
    }
}
