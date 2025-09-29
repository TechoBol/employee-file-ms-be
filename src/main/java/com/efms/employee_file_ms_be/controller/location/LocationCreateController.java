package com.efms.employee_file_ms_be.controller.location;

import com.efms.employee_file_ms_be.api.request.LocationCreateRequest;
import com.efms.employee_file_ms_be.api.response.LocationResponse;
import com.efms.employee_file_ms_be.command.location.LocationCreateCmd;
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
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Tag(name = "Location")
public class LocationCreateController {

    private final LocationCreateCmd command;

    @PostMapping
    @Operation(summary = "Create a new Location")
    public ResponseEntity<LocationResponse> create(@Valid @RequestBody LocationCreateRequest request) {
        command.setLocationCreateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getLocationResponse());
    }
}
