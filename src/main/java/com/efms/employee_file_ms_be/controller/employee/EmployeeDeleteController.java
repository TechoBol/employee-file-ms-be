package com.efms.employee_file_ms_be.controller.employee;

import com.efms.employee_file_ms_be.command.employee.EmployeeDeleteCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee")
public class EmployeeDeleteController {

    private final EmployeeDeleteCmd command;

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an Employee")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        command.setId(id);
        command.execute();

        return ResponseEntity.noContent().build();
    }
}
