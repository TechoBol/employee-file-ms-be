package com.efms.employee_file_ms_be.controller.branch;

import com.efms.employee_file_ms_be.api.response.BranchResponse;
import com.efms.employee_file_ms_be.command.branch.BranchListCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.BRANCH_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.BRANCH)
public class BranchListController {

    private final BranchListCmd command;

    @GetMapping
    @Operation(summary = "Get branches by company ID")
    public ResponseEntity<List<BranchResponse>> getByCompany() {
        command.execute();

        return ResponseEntity.ok(command.getBranches());
    }
}
