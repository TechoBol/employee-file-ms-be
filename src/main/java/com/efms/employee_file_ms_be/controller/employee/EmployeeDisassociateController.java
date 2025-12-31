package com.efms.employee_file_ms_be.controller.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeUpdateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.employee.EmployeeDisassociateCmd;
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
@RequestMapping(Constants.Path.EMPLOYEE_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.EMPLOYEE)
public class EmployeeDisassociateController {

    private final EmployeeDisassociateCmd command;

    @PatchMapping("/{id}/disassociate")
    @Operation(summary = "Disassociate an Employee")
    public ResponseEntity<EmployeeResponse> disassociate(@PathVariable String id,
                                                         @Valid @RequestBody EmployeeUpdateRequest request,
                                                         @RequestHeader(Constants.Header.X_USER_NAME) String userName) {
        command.setId(id);
        command.setEmployeeUpdateRequest(request);
        command.setUserName(userName);
        command.execute();

        return ResponseEntity.ok(command.getEmployeeResponse());
    }
}
