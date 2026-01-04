package com.efms.employee_file_ms_be.model.mapper.salary_event;

import com.efms.employee_file_ms_be.api.request.SalaryEventCreateRequest;
import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.domain.PayrollStatus;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.domain.SalaryEventFrequency;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Component
public class SalaryEventMapper implements CustomMapper<SalaryEventResponse, SalaryEventCreateRequest, SalaryEvent> {

    @Override
    public SalaryEvent toEntity(SalaryEventCreateRequest salaryEventCreateRequest) {
        SalaryEvent instance = new SalaryEvent();
        instance.setType(salaryEventCreateRequest.getType());
        instance.setCategory(salaryEventCreateRequest.getCategory());
        instance.setDescription(salaryEventCreateRequest.getDescription());
        instance.setAmount(salaryEventCreateRequest.getAmount());
        instance.setFrequency(salaryEventCreateRequest.getFrequency());
        instance.setStartDate(salaryEventCreateRequest.getStartDate());
        instance.setEndDate(salaryEventCreateRequest.getEndDate());
        instance.setStatus(PayrollStatus.OPEN);

        Employee employee = new Employee();
        employee.setId(UUID.fromString(salaryEventCreateRequest.getEmployeeId()));
        instance.setEmployee(employee);

        return instance;
    }

    @Override
    public SalaryEventResponse toDTO(SalaryEvent salaryEvent) {
        return SalaryEventResponse.builder()
                .id(salaryEvent.getId().toString())
                .employeeId(salaryEvent.getEmployee().getId().toString())
                .type(String.valueOf(salaryEvent.getType()))
                .category(salaryEvent.getCategory().toString())
                .description(salaryEvent.getDescription())
                .amount(salaryEvent.getAmount())
                .frequency(String.valueOf(salaryEvent.getFrequency()))
                .startDate(salaryEvent.getStartDate())
                .endDate(salaryEvent.getEndDate())
                .build();
    }
}
