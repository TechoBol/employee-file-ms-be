package com.efms.employee_file_ms_be.command.location;

import com.efms.employee_file_ms_be.api.response.LocationResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Location;
import com.efms.employee_file_ms_be.model.mapper.location.LocationMapper;
import com.efms.employee_file_ms_be.model.repository.LocationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class LocationListByCompanyIdCmd implements Command {

    @Setter
    private String companyId;

    @Getter
    private List<LocationResponse> locations;

    private final LocationRepository repository;

    private final LocationMapper mapper;

    @Override
    public void execute() {
        List<Location> locationsByCompany = repository.findLocationsByCompanyId(UUID.fromString(companyId));
        locations = locationsByCompany.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
