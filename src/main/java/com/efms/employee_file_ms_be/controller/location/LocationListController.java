package com.efms.employee_file_ms_be.controller.location;

import com.efms.employee_file_ms_be.api.response.LocationResponse;
import com.efms.employee_file_ms_be.command.location.LocationListCmd;
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
@RequestMapping(Constants.Path.LOCATION_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.LOCATION)
public class LocationListController {

    private final LocationListCmd command;

    @GetMapping
    @Operation(summary = "Get locations by company ID")
    public ResponseEntity<List<LocationResponse>> getByCompany() {
        command.execute();

        return ResponseEntity.ok(command.getLocations());
    }
}