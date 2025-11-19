package com.efms.employee_file_ms_be.api.response.payroll;

import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.model.repository.projection.EmployeeProjection;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class PayrollEmployeeResponse {
    private EmployeeResponse employee;
    private PayrollResponse payroll;
}
