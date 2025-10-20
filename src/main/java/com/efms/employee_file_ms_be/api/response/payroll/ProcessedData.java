package com.efms.employee_file_ms_be.api.response.payroll;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProcessedData {
    private UUID id;
    private UUID employeeId;
    private String details;
}
