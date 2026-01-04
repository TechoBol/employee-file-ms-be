package com.efms.employee_file_ms_be.api.response.payment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class PaymentReprocessSummary {
    private Integer period;
    private UUID companyId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationMs;
    private Long deletedPayments;
    private Long newPaymentsCreated;
    private Long failedPayments;
    private Integer totalEmployees;
    private boolean success;
    private String errorMessage;
}