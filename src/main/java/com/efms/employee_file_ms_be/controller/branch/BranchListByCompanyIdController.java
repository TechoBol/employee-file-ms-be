package com.efms.employee_file_ms_be.controller.branch;

import com.efms.employee_file_ms_be.api.response.BranchResponse;
import com.efms.employee_file_ms_be.command.branch.BranchListByCompanyIdCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
@Tag(name = "Branch")
public class BranchListByCompanyIdController {

    private final BranchListByCompanyIdCmd command;

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get branches by company ID")
    public ResponseEntity<List<BranchResponse>> getByCompany(@PathVariable String companyId) {
        command.setCompanyId(companyId);
        command.execute();

        return ResponseEntity.ok(command.getBranches());
    }
}
