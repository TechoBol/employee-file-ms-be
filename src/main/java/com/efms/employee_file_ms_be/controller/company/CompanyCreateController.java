package com.efms.employee_file_ms_be.controller.company;

import com.efms.employee_file_ms_be.api.request.CompanyCreateRequest;
import com.efms.employee_file_ms_be.api.response.CompanyResponse;
import com.efms.employee_file_ms_be.command.company.CompanyCreateCmd;
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

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.COMPANY_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.COMPANY)
public class CompanyCreateController {

    private final CompanyCreateCmd command;

    @PostMapping
    @Operation(summary = "Create a new Company")
    public ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyCreateRequest request) {
        command.setCompanyCreateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getCompanyResponse());
    }
}
