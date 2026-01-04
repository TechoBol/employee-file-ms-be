package com.efms.employee_file_ms_be.model.mapper;

import com.efms.employee_file_ms_be.api.request.PositionCreateRequest;
import com.efms.employee_file_ms_be.api.response.PositionResponse;
import com.efms.employee_file_ms_be.model.domain.Department;
import com.efms.employee_file_ms_be.model.domain.Position;
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
        Department department = new Department();
        department.setId(UUID.fromString(positionCreateRequest.getDepartmentId()));
        BeanUtils.copyProperties(positionCreateRequest, instance);
        instance.setDepartment(department);
        return instance;
    }

    @Override
    public PositionResponse toDTO(Position position) {
        return PositionResponse.builder()
                .id(position.getId().toString())
                .name(position.getName())
                .description(position.getDescription())
                .departmentId(position.getDepartment().getId().toString())
                .build();
    }
}
