package com.efms.employee_file_ms_be.controller.position;

import com.efms.employee_file_ms_be.api.response.PositionResponse;
import com.efms.employee_file_ms_be.command.position.PositionListByCompanyIdCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
@Tag(name = "Position")
public class PositionListByCompanyIdController {

    private final PositionListByCompanyIdCmd command;

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get positions by company ID")
    public ResponseEntity<List<PositionResponse>> getByCompany(@PathVariable String companyId) {
        command.setCompanyId(companyId);
        command.execute();

        return ResponseEntity.ok(command.getPositions());
    }
}
