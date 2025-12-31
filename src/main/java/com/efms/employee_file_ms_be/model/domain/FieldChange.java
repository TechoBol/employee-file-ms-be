package com.efms.employee_file_ms_be.model.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
public class FieldChange {
    private String fieldName;
    private Object oldValue;
    private Object newValue;
    private LocalDateTime changedAt;
}
