package com.efms.employee_file_ms_be.exception;

import com.efms.employee_file_ms_be.exception.core.ApiRestException;
import com.efms.employee_file_ms_be.exception.core.annotations.NotFoundException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestExceptionAttribute;
import lombok.AllArgsConstructor;

@RestException(
        message = Constants.ExceptionMessage.FILE_NOT_FOUND
)
@NotFoundException
@AllArgsConstructor
public class FileNotFoundException extends ApiRestException {

    @RestExceptionAttribute
    private String employeeId;
}
