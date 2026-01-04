package com.efms.employee_file_ms_be.api.response.payment;

import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.model.domain.EmployeeDetails;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class PaymentEmployeeResponse {
    private EmployeeDetails employee;
    private PaymentDetailsResponse payment;
}
