package com.efms.employee_file_ms_be.model.mapper.absence;

import com.efms.employee_file_ms_be.api.request.AbsenceCreateRequest;
import com.efms.employee_file_ms_be.api.response.AbsenceResponse;
import com.efms.employee_file_ms_be.model.domain.Absence;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Component
public class AbsenceMapper implements CustomMapper<AbsenceResponse, AbsenceCreateRequest, Absence> {

    @Override
    public Absence toEntity(AbsenceCreateRequest absenceCreateRequest) {
        Absence instance = new Absence();
        instance.setType(absenceCreateRequest.getType());
        instance.setDuration(absenceCreateRequest.getDuration());
        instance.setDate(absenceCreateRequest.getDate());
        instance.setEndDate(absenceCreateRequest.getEndDate());
        instance.setReason(absenceCreateRequest.getReason());
        instance.setDescription(absenceCreateRequest.getDescription());

        Employee employee = new Employee();
        employee.setId(UUID.fromString(absenceCreateRequest.getEmployeeId()));
        instance.setEmployee(employee);

        return instance;
    }

    @Override
    public AbsenceResponse toDTO(Absence absence) {
        return AbsenceResponse.builder()
                .id(absence.getId().toString())
                .employeeId(absence.getEmployee().getId().toString())
                .type(String.valueOf(absence.getType()))
                .duration(String.valueOf(absence.getDuration()))
                .date(absence.getDate())
                .endDate(absence.getEndDate())
                .reason(absence.getReason())
                .description(absence.getDescription())
                .salaryEventId(absence.getSalaryEvent() != null ? absence.getSalaryEvent().getId().toString() : null)
                .deductionAmount(absence.getSalaryEvent() != null ? absence.getSalaryEvent().getAmount() : null)
                .createdAt(absence.getCreatedAt())
                .updatedAt(absence.getUpdatedAt())
                .build();
    }
}
