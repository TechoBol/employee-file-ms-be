package com.efms.employee_file_ms_be.exception;

import com.efms.employee_file_ms_be.exception.core.ApiRestException;
import com.efms.employee_file_ms_be.exception.core.annotations.BadRequestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestExceptionAttribute;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Josue Veliz
 */
@RestException(
        message = Constants.ExceptionMessage.END_DATE_BEFORE_DATE
)
@BadRequestException
@AllArgsConstructor
@NoArgsConstructor
public class EndDateBeforeStartDateException extends ApiRestException {

    @RestExceptionAttribute
    private LocalDateTime startDate;

    @RestExceptionAttribute
    private LocalDateTime endDate;
}
