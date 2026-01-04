package com.efms.employee_file_ms_be.model.mapper;

import com.efms.employee_file_ms_be.api.request.DepartmentCreateRequest;
import com.efms.employee_file_ms_be.api.response.DepartmentResponse;
import com.efms.employee_file_ms_be.model.domain.Department;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author Josue Veliz
 */
@Component
public class DepartmentMapper implements CustomMapper<DepartmentResponse, DepartmentCreateRequest, Department> {

    @Override
    public Department toEntity(DepartmentCreateRequest departmentCreateRequest) {
        Department instance = new Department();
        BeanUtils.copyProperties(departmentCreateRequest, instance);
        return instance;
    }

    @Override
    public DepartmentResponse toDTO(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId().toString())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }
}
