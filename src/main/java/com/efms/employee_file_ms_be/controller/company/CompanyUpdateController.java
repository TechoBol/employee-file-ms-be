package com.efms.employee_file_ms_be.controller.company;

import com.efms.employee_file_ms_be.api.request.CompanyUpdateRequest;
import com.efms.employee_file_ms_be.api.response.CompanyResponse;
import com.efms.employee_file_ms_be.command.company.CompanyUpdateCmd;
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
@RequestMapping(Constants.Path.COMPANY_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.COMPANY)
public class CompanyUpdateController {

    private final CompanyUpdateCmd command;

    @PutMapping("/{id}")
    @Operation(summary = "Update a Company")
    public ResponseEntity<CompanyResponse> update(
            @PathVariable String id,
            @Valid @RequestBody CompanyUpdateRequest request) {
        command.setId(id);
        command.setRequest(request);
        command.execute();

        return ResponseEntity.ok(command.getCompanyResponse());
    }
}
