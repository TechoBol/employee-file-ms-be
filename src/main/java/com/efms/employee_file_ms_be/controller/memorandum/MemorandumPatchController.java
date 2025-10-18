package com.efms.employee_file_ms_be.controller.memorandum;

import com.efms.employee_file_ms_be.api.request.MemorandumUpdateRequest;
import com.efms.employee_file_ms_be.api.response.MemorandumResponse;
import com.efms.employee_file_ms_be.command.memorandum.MemorandumPatchCmd;
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
@RequestMapping(Constants.Path.MEMORANDUM_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.MEMORANDUM)
public class MemorandumPatchController {

    private final MemorandumPatchCmd command;

    @PatchMapping("/{id}")
    @Operation(summary = "Update a Memorandum associated an user")
    public ResponseEntity<MemorandumResponse> patchMemorandum(@PathVariable String id,
                                                              @Valid @RequestBody MemorandumUpdateRequest request) {
        command.setId(id);
        command.setMemorandumUpdateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.OK).body(command.getMemorandumResponse());
    }
}
