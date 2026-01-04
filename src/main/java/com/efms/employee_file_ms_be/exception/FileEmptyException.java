package com.efms.employee_file_ms_be.exception;

import com.efms.employee_file_ms_be.exception.core.ApiRestException;
import com.efms.employee_file_ms_be.exception.core.annotations.BadRequestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestExceptionAttribute;
import lombok.AllArgsConstructor;

/**
 * @author Josue Veliz
 */
@RestException(
        message = Constants.ExceptionMessage.FILE_EMPTY_EXCEPTION
)
@BadRequestException
@AllArgsConstructor
public class FileEmptyException extends ApiRestException {

    @RestExceptionAttribute
    private String fileName;
}
