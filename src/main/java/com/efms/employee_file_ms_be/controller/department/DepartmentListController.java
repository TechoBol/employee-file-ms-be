package com.efms.employee_file_ms_be.controller.department;

import com.efms.employee_file_ms_be.api.response.DepartmentResponse;
import com.efms.employee_file_ms_be.command.department.DepartmentListCmd;
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
@RequestMapping(Constants.Path.DEPARTMENT_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.DEPARTMENT)
public class DepartmentListController {

    private final DepartmentListCmd command;

    @GetMapping("")
    @Operation(summary = "Get departments by company ID")
    public ResponseEntity<List<DepartmentResponse>> getByCompany() {
        command.execute();

        return ResponseEntity.ok(command.getDepartments());
    }
}
