package com.efms.employee_file_ms_be.exception;

import com.efms.employee_file_ms_be.exception.core.ApiRestException;
import com.efms.employee_file_ms_be.exception.core.annotations.BadRequestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestException;

/**
 * @author Josue Veliz
 */
@RestException(
        message = Constants.ExceptionMessage.NO_FILES_PROVIDED_EXCEPTION
)
@BadRequestException
public class NoFilesProvidedException extends ApiRestException {
}