package com.efms.employee_file_ms_be.exception.core.processor;

import com.efms.employee_file_ms_be.exception.Constants;
import com.efms.employee_file_ms_be.exception.core.ApiRestException;
import com.efms.employee_file_ms_be.exception.core.RestErrorResponse;
import com.efms.employee_file_ms_be.exception.core.annotations.BadRequestException;
import com.efms.employee_file_ms_be.exception.core.annotations.NotFoundException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestException;
import com.efms.employee_file_ms_be.exception.core.annotations.RestExceptionAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Josue Veliz
 */
@Component
@Slf4j
public class RestExceptionProcessor {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^}]+)}");

    public RestErrorResponse processException(ApiRestException exception) {
        Class<?> exceptionClass = exception.getClass();

        int statusCode = determineHttpStatus(exceptionClass);

        Map<String, Object> attributes = extractAttributes(exception);

        String message = extractAndInterpolateMessage(exceptionClass, attributes);

        RestErrorResponse response = new RestErrorResponse(statusCode, message);
        response.setAttributes(attributes);

        return response;
    }

    private int determineHttpStatus(Class<?> exceptionClass) {
        if (exceptionClass.isAnnotationPresent(NotFoundException.class)) {
            return HttpStatus.NOT_FOUND.value();
        } else if (exceptionClass.isAnnotationPresent(BadRequestException.class)) {
            return HttpStatus.BAD_REQUEST.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    private String extractAndInterpolateMessage(Class<?> exceptionClass, Map<String, Object> attributes) {
        RestException restException = exceptionClass.getAnnotation(RestException.class);
        if (restException != null) {
            String messageTemplate = restException.message();
            return interpolateMessage(messageTemplate, attributes);
        }
        return HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    }

    private String interpolateMessage(String messageTemplate, Map<String, Object> attributes) {
        if (messageTemplate == null || messageTemplate.trim().isEmpty()) {
            return messageTemplate;
        }

        String result = messageTemplate;

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(messageTemplate);

        while (matcher.find()) {
            String placeholder = matcher.group(0);
            String variableName = matcher.group(1);

            Object value = attributes.get(variableName);
            String replacement = value != null ? value.toString() : placeholder;

            result = result.replace(placeholder, replacement);
        }

        return result;
    }

    private Map<String, Object> extractAttributes(ApiRestException exception) {
        Map<String, Object> attributes = new HashMap<>();
        Class<?> clazz = exception.getClass();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RestExceptionAttribute.class)) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(exception);

                    RestExceptionAttribute annotation = field.getAnnotation(RestExceptionAttribute.class);
                    String attributeName = getAttributeName(field, annotation);

                    if (value != null) {
                        attributes.put(attributeName, value);
                    }
                } catch (IllegalAccessException e) {
                    log.error(String.format(Constants.ExceptionMessage.ERROR_ACCESSING_FIELDS, field), e.getMessage());
                }
            }
        }

        return attributes;
    }

    private String getAttributeName(Field field, RestExceptionAttribute annotation) {
        String alias = annotation.alias();
        return (alias != null && !alias.trim().isEmpty()) ? alias : field.getName();
    }
}
