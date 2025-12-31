package com.efms.employee_file_ms_be.controller.advance;

import com.efms.employee_file_ms_be.api.request.AdvanceUpdateRequest;
import com.efms.employee_file_ms_be.api.response.AdvanceResponse;
import com.efms.employee_file_ms_be.command.advance.AdvanceReplacePatchCmd;
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
@RequestMapping(Constants.Path.ADVANCE_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.ADVANCE)
public class AdvanceReplacePatchController {

    private final AdvanceReplacePatchCmd command;

    @PatchMapping("/{id}/replace")
    @Operation(summary = "Replace an Advance by deleting and recreating it with updated data")
    public ResponseEntity<AdvanceResponse> replaceAdvance(@PathVariable String id,
                                                          @Valid @RequestBody AdvanceUpdateRequest request) {
        command.setId(id);
        command.setAdvanceUpdateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.OK).body(command.getAdvanceResponse());
    }
}
