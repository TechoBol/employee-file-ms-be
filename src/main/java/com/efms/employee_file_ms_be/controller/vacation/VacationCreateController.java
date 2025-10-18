package com.efms.employee_file_ms_be.controller.vacation;

import com.efms.employee_file_ms_be.api.request.VacationCreateRequest;
import com.efms.employee_file_ms_be.api.response.VacationResponse;
import com.efms.employee_file_ms_be.command.vacation.VacationCreateCmd;
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
@RequestMapping(Constants.Path.VACATION_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.VACATION)
public class VacationCreateController {

    private final VacationCreateCmd command;

    @PostMapping
    @Operation(summary = "Create a new Vacation associated an user")
    public ResponseEntity<VacationResponse> createVacation(@Valid @RequestBody VacationCreateRequest request) {
        command.setVacationCreateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getVacationResponse());
    }
}