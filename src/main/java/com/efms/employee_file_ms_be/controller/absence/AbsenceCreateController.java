package com.efms.employee_file_ms_be.controller.absence;

import com.efms.employee_file_ms_be.api.request.AbsenceCreateRequest;
import com.efms.employee_file_ms_be.api.response.AbsenceResponse;
import com.efms.employee_file_ms_be.command.absence.AbsenceCreateCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.ABSENCES_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.ABSENCE)
public class AbsenceCreateController {

    private final AbsenceCreateCmd command;

    @PostMapping
    @Operation(summary = "Create a new Absence associated an user")
    public ResponseEntity<AbsenceResponse> createAbsence(@Valid @RequestBody AbsenceCreateRequest request) {
        command.setAbsenceCreateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getAbsenceResponse());
    }
}
