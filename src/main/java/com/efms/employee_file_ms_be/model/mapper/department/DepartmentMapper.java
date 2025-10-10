package com.efms.employee_file_ms_be.model.mapper.department;

import com.efms.employee_file_ms_be.api.request.DepartmentCreateRequest;
import com.efms.employee_file_ms_be.api.response.DepartmentResponse;
import com.efms.employee_file_ms_be.model.domain.Company;
import com.efms.employee_file_ms_be.model.domain.Department;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
                .build();
    }
}
