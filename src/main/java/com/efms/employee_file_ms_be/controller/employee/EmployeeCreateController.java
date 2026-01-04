package com.efms.employee_file_ms_be.controller.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeCreateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.employee.EmployeeCreateCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.EMPLOYEE_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.EMPLOYEE)
public class EmployeeCreateController {

    private final EmployeeCreateCmd command;

    @PostMapping
    @Operation(summary = "Create a new Employee")
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeCreateRequest request,
                                                   @RequestParam(name = "includeDetails", defaultValue = "true") boolean includeDetails,
                                                   @RequestHeader(Constants.Header.X_USER_NAME) String userName) {
        command.setEmployeeCreateRequest(request);
        command.setIncludeDetails(includeDetails);
        command.setUserName(userName);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getResponse());
    }
}
