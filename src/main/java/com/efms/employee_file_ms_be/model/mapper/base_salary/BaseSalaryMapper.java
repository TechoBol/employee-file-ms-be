package com.efms.employee_file_ms_be.model.mapper.base_salary;

import com.efms.employee_file_ms_be.api.request.BaseSalaryCreateRequest;
import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.model.domain.BaseSalary;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Component
public class BaseSalaryMapper implements CustomMapper<BaseSalaryResponse, BaseSalaryCreateRequest, BaseSalary> {

    @Override
    public BaseSalary toEntity(BaseSalaryCreateRequest baseSalaryCreateRequest) {
        Employee employee = new Employee();
        employee.setId(UUID.fromString(baseSalaryCreateRequest.getEmployeeId()));
        BaseSalary instance = new BaseSalary();
        instance.setAmount(baseSalaryCreateRequest.getAmount());
        instance.setStartDate(baseSalaryCreateRequest.getStartDate());
        instance.setEndDate(baseSalaryCreateRequest.getEndDate());
        instance.setEmployee(employee);
        return instance;
    }

    @Override
    public BaseSalaryResponse toDTO(BaseSalary baseSalary) {
        return BaseSalaryResponse.builder()
                .amount(baseSalary.getAmount())
                .startDate(baseSalary.getStartDate())
                .endDate(baseSalary.getEndDate())
                .employeeId(baseSalary.getEmployee().getId().toString())
                .build();
    }
}
