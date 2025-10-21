package com.efms.employee_file_ms_be.model.mapper.advance;

import com.efms.employee_file_ms_be.api.request.AdvanceCreateRequest;
import com.efms.employee_file_ms_be.api.response.AdvanceResponse;
import com.efms.employee_file_ms_be.model.domain.Advance;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Component
public class AdvanceMapper implements CustomMapper<AdvanceResponse, AdvanceCreateRequest, Advance> {

    @Override
    public AdvanceResponse toDTO(Advance advance) {
        return AdvanceResponse.builder()
                .id(advance.getId())
                .employeeId(String.valueOf(advance.getEmployee().getId()))
                .amount(advance.getAmount())
                .advanceDate(advance.getAdvanceDate())
                .build();
    }

    @Override
    public Advance toEntity(AdvanceCreateRequest dto) {
        Advance instance = new Advance();
        BeanUtils.copyProperties(dto, instance);

        Employee employee = new Employee();
        employee.setId(UUID.fromString(dto.getEmployeeId()));
        instance.setEmployee(employee);

        return instance;
    }
}
