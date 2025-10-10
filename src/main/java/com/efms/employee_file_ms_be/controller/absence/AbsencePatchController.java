package com.efms.employee_file_ms_be.controller.absence;

import com.efms.employee_file_ms_be.api.request.AbsenceUpdateRequest;
import com.efms.employee_file_ms_be.api.response.AbsenceResponse;
import com.efms.employee_file_ms_be.command.absence.AbsencePatchCmd;
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
@RequestMapping(Constants.Path.ABSENCES_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.ABSENCE)
public class AbsencePatchController {

    private final AbsencePatchCmd command;

    @PatchMapping("/{id}")
    @Operation(summary = "Update a Absence associated an user")
    public ResponseEntity<AbsenceResponse> patchAbsence(@PathVariable String id,
                                                        @Valid @RequestBody AbsenceUpdateRequest request) {
        command.setId(id);
        command.setAbsenceUpdateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.OK).body(command.getAbsenceResponse());
    }
}
