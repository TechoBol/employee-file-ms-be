package com.efms.employee_file_ms_be.controller.department;

import com.efms.employee_file_ms_be.api.response.DepartmentResponse;
import com.efms.employee_file_ms_be.command.department.DepartmentListByCompanyIdCmd;
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
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Department")
public class DepartmentListByCompanyIdController {

    private final DepartmentListByCompanyIdCmd command;

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get departments by company ID")
    public ResponseEntity<List<DepartmentResponse>> getByCompany(@PathVariable String companyId) {
        command.setCompanyId(companyId);
        command.execute();

        return ResponseEntity.ok(command.getDepartments());
    }
}
