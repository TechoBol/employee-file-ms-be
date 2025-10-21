package com.efms.employee_file_ms_be.api.response.payment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class CompanyProcessResult {
    private UUID companyId;
    private String companyName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean success;
    private String errorMessage;
    private int totalEmployees;
    private int successfulPayments;
    private int failedPayments;
    private int processedAdvances;
    private int processedAbsences;
    private int processedEvents;

    public void incrementSuccessfulPayments() {
        this.successfulPayments++;
    }

    public void incrementFailedPayments() {
        this.failedPayments++;
    }
}
