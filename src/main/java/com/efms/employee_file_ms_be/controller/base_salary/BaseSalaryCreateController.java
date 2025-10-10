package com.efms.employee_file_ms_be.controller.base_salary;

import com.efms.employee_file_ms_be.api.request.BaseSalaryCreateRequest;
import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.base_salary.BaseSalaryCreateCmd;
import com.efms.employee_file_ms_be.controller.Constants;
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

@RestController
@RequestMapping(Constants.Path.BASE_SALARY_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.BASE_SALARY)
public class BaseSalaryCreateController {

    private final BaseSalaryCreateCmd command;

    @PostMapping
    @Operation(summary = "Create a new Base Salary and associate an user")
    public ResponseEntity<BaseSalaryResponse> create(@Valid @RequestBody BaseSalaryCreateRequest request) {
        command.setBaseSalaryCreateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getBaseSalaryResponse());
    }
}
