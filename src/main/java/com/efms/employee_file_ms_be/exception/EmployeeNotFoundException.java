package com.efms.employee_file_ms_be.exception;


import com.efms.employee_file_ms_be.exception.core.ApiRestException;
import com.efms.employee_file_ms_be.exception.core.annotations.NotFoundException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestExceptionAttribute;
import lombok.AllArgsConstructor;

/**
 * @author Josue Veliz
 */
@RestException(
        message = Constants.ExceptionMessage.EMPLOYEE_NOT_FOUND
)
@NotFoundException()
@AllArgsConstructor
public class EmployeeNotFoundException extends ApiRestException {

    @RestExceptionAttribute
    private final String employeeId;
}
