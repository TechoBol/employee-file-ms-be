package com.efms.employee_file_ms_be.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

    public int getTotalDays() {
        if (endDate == null) {
            return 1;
        }
        return (int) ChronoUnit.DAYS.between(LocalDate.parse(date.toString()), endDate.plusDays(1));
    }
}
