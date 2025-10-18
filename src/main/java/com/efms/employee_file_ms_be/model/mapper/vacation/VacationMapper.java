package com.efms.employee_file_ms_be.model.mapper.vacation;

import com.efms.employee_file_ms_be.api.request.VacationCreateRequest;
import com.efms.employee_file_ms_be.api.response.VacationResponse;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.domain.Vacation;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Component
public class VacationMapper implements CustomMapper<VacationResponse, VacationCreateRequest, Vacation> {

    @Override
    public VacationResponse toDTO(Vacation vacation) {
        return VacationResponse.builder()
                .id(vacation.getId())
                .employeeId(String.valueOf(vacation.getEmployee().getId()))
                .startDate(vacation.getStartDate())
                .endDate(vacation.getEndDate())
                .notes(vacation.getNotes())
                .build();
    }

    @Override
    public Vacation toEntity(VacationCreateRequest dto) {
        Vacation instance = new Vacation();
        BeanUtils.copyProperties(dto, instance);

        Employee employee = new Employee();
        employee.setId(UUID.fromString(dto.getEmployeeId()));
        instance.setEmployee(employee);

        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            long days = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;
            instance.setDaysTaken((int) days);
        }

        return instance;
    }
}
