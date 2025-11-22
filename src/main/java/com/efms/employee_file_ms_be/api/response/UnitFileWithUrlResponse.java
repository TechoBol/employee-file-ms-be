package com.efms.employee_file_ms_be.api.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class UnitFileWithUrlResponse {
    private UUID id;
    private String originalName;
    private String section;
    private String description;
    private String uploadBy;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
