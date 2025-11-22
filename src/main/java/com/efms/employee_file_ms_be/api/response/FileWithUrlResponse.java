package com.efms.employee_file_ms_be.api.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class FileWithUrlResponse {
    private UUID id;
    private UUID employeeId;
    private UUID companyId;
    private List<UnitFileWithUrlResponse> sections;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
