package com.efms.employee_file_ms_be.model.mapper.memorandum;

import com.efms.employee_file_ms_be.api.request.MemorandumCreateRequest;
import com.efms.employee_file_ms_be.api.response.MemorandumResponse;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.domain.Memorandum;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Component
public class MemorandumMapper implements CustomMapper<MemorandumResponse, MemorandumCreateRequest, Memorandum> {

    @Override
    public MemorandumResponse toDTO(Memorandum memorandum) {
        return MemorandumResponse.builder()
                .id(memorandum.getId())
                .employeeId(String.valueOf(memorandum.getEmployee().getId()))
                .type(memorandum.getType())
                .description(memorandum.getDescription())
                .memorandumDate(memorandum.getMemorandumDate())
                .isPositive(memorandum.getIsPositive())
                .build();
    }

    @Override
    public Memorandum toEntity(MemorandumCreateRequest dto) {
        Memorandum instance = new Memorandum();
        BeanUtils.copyProperties(dto, instance);

        Employee employee = new Employee();
        employee.setId(UUID.fromString(dto.getEmployeeId()));
        instance.setEmployee(employee);

        return instance;
    }
}
