package com.efms.employee_file_ms_be.command.location;

import com.efms.employee_file_ms_be.api.request.LocationUpdateRequest;
import com.efms.employee_file_ms_be.api.response.LocationResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.exception.LocationNotFoundException;
import com.efms.employee_file_ms_be.model.domain.Branch;
import com.efms.employee_file_ms_be.model.domain.Location;
import com.efms.employee_file_ms_be.model.mapper.location.LocationMapper;
import com.efms.employee_file_ms_be.model.repository.LocationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class LocationPatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private LocationUpdateRequest locationUpdateRequest;

    @Getter
    private LocationResponse locationResponse;

    private final LocationRepository repository;

    private final LocationMapper mapper;

    @Override
    public void execute() {
        Location location = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new LocationNotFoundException(id));
        updateProperties(location, locationUpdateRequest);
        location = repository.save(location);
        locationResponse = mapper.toDTO(location);
    }

    private void updateProperties(Location location, LocationUpdateRequest locationUpdateRequest) {
        Optional.ofNullable(locationUpdateRequest.getName()).ifPresent(location::setName);
        Optional.ofNullable(locationUpdateRequest.getBranchId()).ifPresent(branchId -> {
            Branch branch = new Branch();
            branch.setId(UUID.fromString(branchId));
            location.setBranch(branch);
        });
    }
}
