package com.efms.employee_file_ms_be.exception.core;

import com.efms.employee_file_ms_be.exception.core.processor.RestExceptionProcessor;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Josue Veliz
 */
@RestControllerAdvice
@AllArgsConstructor
public class RestErrorHandler {

    private final RestExceptionProcessor exceptionProcessor;

    @ExceptionHandler(ApiRestException.class)
    public ResponseEntity<RestErrorResponse> handleApiRestException(ApiRestException ex) {
        RestErrorResponse errorResponse = exceptionProcessor.processException(ex);
        return ResponseEntity.status(errorResponse.getCode()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorResponse> handleGenericException(Exception ex) {
        RestErrorResponse errorResponse = new RestErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
