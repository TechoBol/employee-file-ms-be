package com.efms.employee_file_ms_be.command.location;

import com.efms.employee_file_ms_be.api.request.LocationCreateRequest;
import com.efms.employee_file_ms_be.api.response.LocationResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Location;
import com.efms.employee_file_ms_be.model.mapper.location.LocationMapper;
import com.efms.employee_file_ms_be.model.repository.LocationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class LocationCreateCmd implements Command {

    @Setter
    private LocationCreateRequest locationCreateRequest;

    @Getter
    private LocationResponse locationResponse;

    private final LocationRepository repository;

    private final LocationMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Location location = mapper.toEntity(locationCreateRequest);
        location.setCompanyId(companyId);
        location = repository.save(location);
        locationResponse = mapper.toDTO(location);
    }
}
