package com.efms.employee_file_ms_be.model.mapper.position;

import com.efms.employee_file_ms_be.api.request.PositionCreateRequest;
import com.efms.employee_file_ms_be.api.response.PositionResponse;
import com.efms.employee_file_ms_be.model.domain.Company;
import com.efms.employee_file_ms_be.model.domain.Position;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Component
public class PositionMapper implements CustomMapper<PositionResponse, PositionCreateRequest, Position> {

    @Override
    public Position toEntity(PositionCreateRequest positionCreateRequest) {
        Position instance = new Position();
        Company company = new Company();
        company.setId(UUID.fromString(positionCreateRequest.getCompanyId()));
        BeanUtils.copyProperties(positionCreateRequest, instance);
        instance.setCompany(company);
        return instance;
    }

    @Override
    public PositionResponse toDTO(Position position) {
        return PositionResponse.builder()
                .id(position.getId().toString())
                .name(position.getName())
                .build();
    }
}
