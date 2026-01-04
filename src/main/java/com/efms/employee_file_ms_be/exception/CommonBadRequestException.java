package com.efms.employee_file_ms_be.exception;

import com.efms.employee_file_ms_be.exception.core.ApiRestException;
import com.efms.employee_file_ms_be.exception.core.annotations.BadRequestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestExceptionAttribute;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Josue Veliz
 */
@RestException(
        message = Constants.ExceptionMessage.COMMON_BAD_REQUEST
)
@BadRequestException()
@AllArgsConstructor
public class CommonBadRequestException extends ApiRestException {

    @RestExceptionAttribute
    private final String message;
}
