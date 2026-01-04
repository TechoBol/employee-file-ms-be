package com.efms.employee_file_ms_be.controller.position;

import com.efms.employee_file_ms_be.api.request.PositionCreateRequest;
import com.efms.employee_file_ms_be.api.response.PositionResponse;
import com.efms.employee_file_ms_be.command.position.PositionCreateCmd;
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
@RequestMapping(Constants.Path.POSITION_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.POSITION)
public class PositionCreateController {

    private final PositionCreateCmd command;

    @PostMapping
    @Operation(summary = "Create a new Position")
    public ResponseEntity<PositionResponse> create(@Valid @RequestBody PositionCreateRequest request) {
        command.setPositionCreateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getPositionResponse());
    }
}
