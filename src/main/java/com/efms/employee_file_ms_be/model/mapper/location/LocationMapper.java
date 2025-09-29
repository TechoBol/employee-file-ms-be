package com.efms.employee_file_ms_be.model.mapper.location;

import com.efms.employee_file_ms_be.api.request.LocationCreateRequest;
import com.efms.employee_file_ms_be.api.response.LocationResponse;
import com.efms.employee_file_ms_be.model.domain.Branch;
import com.efms.employee_file_ms_be.model.domain.Location;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Component
public class LocationMapper implements CustomMapper<LocationResponse, LocationCreateRequest, Location> {

    @Override
    public Location toEntity(LocationCreateRequest locationCreateRequest) {
        Location instance = new Location();
        instance.setName(locationCreateRequest.getName());

        Branch branch = new Branch();
        branch.setId(UUID.fromString(locationCreateRequest.getBranchId()));
        instance.setBranch(branch);

        return instance;
    }

    @Override
    public LocationResponse toDTO(Location location) {
        return LocationResponse.builder()
                .id(location.getId().toString())
                .name(location.getName())
                .branchId(location.getBranch().getId().toString())
//                .branchName(location.getBranch().getName())
                .build();
    }
}
