package com.efms.employee_file_ms_be.exception.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestErrorResponse {

    private Integer code;
    private String message;
    private Map<String, Object> attributes;

    public RestErrorResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.attributes = new HashMap<>();
    }

    public void addAttribute(String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }
}