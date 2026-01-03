package com.efms.employee_file_ms_be.controller.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeChangeCompanyRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.employee.EmployeeChangeCompanyCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.EMPLOYEE_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.EMPLOYEE)
public class EmployeeChangeCompanyController {

    private final EmployeeChangeCompanyCmd command;

    @PatchMapping("/{employeeId}/change-company")
    @Operation(summary = "Change employee's company")
    public ResponseEntity<EmployeeResponse> changeCompany(
            @PathVariable UUID employeeId,
            @Valid @RequestBody EmployeeChangeCompanyRequest request,
            @RequestHeader(Constants.Header.X_USER_NAME) String userName) {

        command.setEmployeeId(employeeId);
        command.setNewCompanyId(request.getNewCompanyId());
        command.setNewCompanyName(request.getNewCompanyName());
        command.setReason(request.getReason());
        command.setUserName(userName);
        command.execute();

        return ResponseEntity.ok(command.getResponse());
    }
}
