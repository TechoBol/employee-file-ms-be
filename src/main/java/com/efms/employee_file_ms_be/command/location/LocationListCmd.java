package com.efms.employee_file_ms_be.command.location;

import com.efms.employee_file_ms_be.api.response.LocationResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Location;
import com.efms.employee_file_ms_be.model.mapper.location.LocationMapper;
import com.efms.employee_file_ms_be.model.repository.LocationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class LocationListCmd implements Command {

    @Getter
    private List<LocationResponse> locations;

    private final LocationRepository repository;

    private final LocationMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        List<Location> locationsByCompany = repository.findAllByBranchCompanyId(companyId);
        locations = locationsByCompany.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
