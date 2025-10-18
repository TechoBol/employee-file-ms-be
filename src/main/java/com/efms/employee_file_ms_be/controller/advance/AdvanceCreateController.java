package com.efms.employee_file_ms_be.controller.advance;

import com.efms.employee_file_ms_be.api.request.AdvanceCreateRequest;
import com.efms.employee_file_ms_be.api.response.AdvanceResponse;
import com.efms.employee_file_ms_be.command.advance.AdvanceCreateCmd;
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
@RequestMapping(Constants.Path.ADVANCE_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.ADVANCE)
public class AdvanceCreateController {

    private final AdvanceCreateCmd command;

    @PostMapping
    @Operation(summary = "Create a new Advance associated an user")
    public ResponseEntity<AdvanceResponse> createAdvance(@Valid @RequestBody AdvanceCreateRequest request) {
        command.setAdvanceCreateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getAdvanceResponse());
    }
}
