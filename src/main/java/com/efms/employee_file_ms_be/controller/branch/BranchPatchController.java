package com.efms.employee_file_ms_be.controller.branch;

import com.efms.employee_file_ms_be.api.request.BranchUpdateRequest;
import com.efms.employee_file_ms_be.api.response.BranchResponse;
import com.efms.employee_file_ms_be.command.branch.BranchPatchCmd;
import com.efms.employee_file_ms_be.controller.Constants;
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
@RequestMapping(Constants.Path.BRANCH_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.BRANCH)
public class BranchPatchController {

    private final BranchPatchCmd command;

    @PatchMapping("/{id}")
    @Operation(summary = "Update a Branch")
    public ResponseEntity<BranchResponse> patch(@PathVariable String id,
                                                @Valid @RequestBody BranchUpdateRequest request) {
        command.setId(id);
        command.setBranchUpdateRequest(request);
        command.execute();

        return ResponseEntity.ok(command.getBranchResponse());
    }
}
