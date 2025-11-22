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
public class UnitFileResponse {
    private UUID id;
    private String uuidFileName;
    private String originalName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String section;
}
