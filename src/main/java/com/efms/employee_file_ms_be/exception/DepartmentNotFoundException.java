package com.efms.employee_file_ms_be.exception;

import com.efms.employee_file_ms_be.exception.core.ApiRestException;
import com.efms.employee_file_ms_be.exception.core.annotations.NotFoundException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestExceptionAttribute;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Josue Veliz
 */
@RestException(
        message = Constants.ExceptionMessage.DEPARTMENT_NOT_FOUND
)
@NotFoundException()
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentNotFoundException extends ApiRestException {

    @RestExceptionAttribute
    private String departmentId;
}
