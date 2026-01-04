package com.efms.employee_file_ms_be.controller.employee;

import com.efms.employee_file_ms_be.command.employee.EmployeeDeleteCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.EMPLOYEE_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.EMPLOYEE)
public class EmployeeDeleteController {

    private final EmployeeDeleteCmd command;

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an Employee")
    public ResponseEntity<Void> delete(@PathVariable String id,
                                       @RequestHeader(Constants.Header.X_USER_NAME) String userName) {
        command.setId(id);
        command.setUserName(userName);
        command.execute();

        return ResponseEntity.noContent().build();
    }
}
