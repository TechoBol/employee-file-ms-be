package com.efms.employee_file_ms_be.api.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldChangeResponse {
    private String fieldName;
    private Object oldValue;
    private Object newValue;
    private LocalDateTime changedAt;
    private String displayName;
}