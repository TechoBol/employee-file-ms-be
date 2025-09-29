package com.efms.employee_file_ms_be.controller.location;

import com.efms.employee_file_ms_be.api.response.LocationResponse;
import com.efms.employee_file_ms_be.command.location.LocationListByCompanyIdCmd;
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
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Tag(name = "Location")
public class LocationListByCompanyIdController {

    private final LocationListByCompanyIdCmd command;

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get locations by company ID")
    public ResponseEntity<List<LocationResponse>> getByCompany(@PathVariable String companyId) {
        command.setCompanyId(companyId);
        command.execute();

        return ResponseEntity.ok(command.getLocations());
    }
}