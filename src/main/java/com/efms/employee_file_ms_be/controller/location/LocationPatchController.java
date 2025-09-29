package com.efms.employee_file_ms_be.controller.location;

import com.efms.employee_file_ms_be.api.request.LocationUpdateRequest;
import com.efms.employee_file_ms_be.api.response.LocationResponse;
import com.efms.employee_file_ms_be.command.location.LocationPatchCmd;
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
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Tag(name = "Location")
public class LocationPatchController {

    private final LocationPatchCmd command;

    @PatchMapping("/{id}")
    @Operation(summary = "Update a Location")
    public ResponseEntity<LocationResponse> patch(
            @PathVariable String id,
            @Valid @RequestBody LocationUpdateRequest request) {
        command.setId(id);
        command.setLocationUpdateRequest(request);
        command.execute();

        return ResponseEntity.ok(command.getLocationResponse());
    }
}
