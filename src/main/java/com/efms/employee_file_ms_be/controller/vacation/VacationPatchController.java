package com.efms.employee_file_ms_be.controller.vacation;

import com.efms.employee_file_ms_be.api.request.VacationUpdateRequest;
import com.efms.employee_file_ms_be.api.response.VacationResponse;
import com.efms.employee_file_ms_be.command.vacation.VacationPatchCmd;
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
@RequestMapping(Constants.Path.VACATION_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.VACATION)
public class VacationPatchController {

    private final VacationPatchCmd command;

    @PatchMapping("/{id}")
    @Operation(summary = "Update a Vacation associated an user")
    public ResponseEntity<VacationResponse> patchVacation(@PathVariable String id,
                                                          @Valid @RequestBody VacationUpdateRequest request) {
        command.setId(id);
        command.setVacationUpdateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.OK).body(command.getVacationResponse());
    }
}
