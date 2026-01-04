package com.efms.employee_file_ms_be.exception.core;

import org.springframework.http.HttpStatus;

/**
 * @author Josue Veliz
 */
public abstract class ApiRestException extends RuntimeException {

    public ApiRestException() {
        super();
    }

    public ApiRestException(String message) {
        super(message);
    }

    public ApiRestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestErrorResponse getDefaultResponse() {
        RestErrorResponse response = new RestErrorResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return response;
    }
}
