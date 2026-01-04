package com.efms.employee_file_ms_be.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
@Builder
public class AbsenceResponse {
    private String id;
    private String employeeId;
    private String type;
    private String duration;
    private LocalDate date;
    private LocalDate endDate;
    private String reason;
    private String description;
    private String salaryEventId;
    private BigDecimal deductionAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean processed = false;
}
