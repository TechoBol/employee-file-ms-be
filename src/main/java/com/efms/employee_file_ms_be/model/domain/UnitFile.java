package com.efms.employee_file_ms_be.model.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UnitFile {
    private UUID id;
    private String url;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String uploadBy;
    private String section;
}
