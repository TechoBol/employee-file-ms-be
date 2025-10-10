package com.efms.employee_file_ms_be.controller.company;

import com.efms.employee_file_ms_be.api.response.CompanyResponse;
import com.efms.employee_file_ms_be.command.company.CompanyListCmd;
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
@RequestMapping(Constants.Path.COMPANY_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.COMPANY)
public class CompanyListController {

    private final CompanyListCmd command;

    @GetMapping
    @Operation(summary = "Get all companies")
    public ResponseEntity<List<CompanyResponse>> getAll() {
        command.execute();

        return ResponseEntity.ok(command.getCompanies());
    }
}
