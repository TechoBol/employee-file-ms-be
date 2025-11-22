package com.efms.employee_file_ms_be.exception;

import com.efms.employee_file_ms_be.exception.core.ApiRestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestExceptionAttribute;
import lombok.AllArgsConstructor;

/**
 * @author Josue Veliz
 */
@RestException(
        message = Constants.ExceptionMessage.FILE_DELETE_EXCEPTION
)
@AllArgsConstructor
public class FileDeleteException extends ApiRestException {

    @RestExceptionAttribute
    private String uuidFileName;

    @RestExceptionAttribute
    private String error;
}
