package com.efms.employee_file_ms_be.exception;

import com.efms.employee_file_ms_be.exception.core.ApiRestException;
import com.efms.employee_file_ms_be.exception.core.annotations.BadRequestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestException;
import lombok.AllArgsConstructor;

/**
 * @author Josue Veliz
 */
@RestException(
        message = Constants.ExceptionMessage.RECORD_EDIT_NOT_ALLOWED
)
@BadRequestException()
@AllArgsConstructor
public class RecordEditNotAllowedException extends ApiRestException {
}
