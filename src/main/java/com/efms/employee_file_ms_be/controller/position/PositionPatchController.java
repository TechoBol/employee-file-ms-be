package com.efms.employee_file_ms_be.controller.position;

import com.efms.employee_file_ms_be.api.request.PositionUpdateRequest;
import com.efms.employee_file_ms_be.api.response.PositionResponse;
import com.efms.employee_file_ms_be.command.position.PositionPatchCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
@Tag(name = "Position")
public class PositionPatchController {

    private final PositionPatchCmd command;

    @PatchMapping("/{id}")
    @Operation(summary = "Update a Position")
    public ResponseEntity<PositionResponse> patch(
            @PathVariable String id,
            @Valid @RequestBody PositionUpdateRequest request) {
        command.setId(id);
        command.setPositionUpdateRequest(request);
        command.execute();

        return ResponseEntity.ok(command.getPositionResponse());
    }
}
