package com.efms.employee_file_ms_be.controller.branch;

import com.efms.employee_file_ms_be.api.request.BranchCreateRequest;
import com.efms.employee_file_ms_be.api.response.BranchResponse;
import com.efms.employee_file_ms_be.command.branch.BranchCreateCmd;
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
@RequestMapping("/api/branches")
@RequiredArgsConstructor
@Tag(name = "Branch")
public class BranchCreateController {

    private final BranchCreateCmd command;

    @PostMapping
    @Operation(summary = "Create a new Branch")
    public ResponseEntity<BranchResponse> create(@Valid @RequestBody BranchCreateRequest request) {
        command.setBranchCreateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getBranchResponse());
    }
}
