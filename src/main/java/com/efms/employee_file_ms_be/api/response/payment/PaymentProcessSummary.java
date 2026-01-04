package com.efms.employee_file_ms_be.api.response.payment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josue Veliz
 */

@Getter
@Setter
public class PaymentProcessSummary {
    private Integer period;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationMs;
    private List<CompanyProcessResult> companyResults = new ArrayList<>();

    public void addCompanyResult(CompanyProcessResult result) {
        this.companyResults.add(result);
    }

    public int getTotalEmployees() {
        return companyResults.stream()
                .mapToInt(CompanyProcessResult::getTotalEmployees)
                .sum();
    }

    public long getTotalSuccessfulPayments() {
        return companyResults.stream()
                .mapToInt(CompanyProcessResult::getSuccessfulPayments)
                .sum();
    }

    public long getTotalFailedPayments() {
        return companyResults.stream()
                .mapToInt(CompanyProcessResult::getFailedPayments)
                .sum();
    }

    public double getSuccessRate() {
        long total = getTotalSuccessfulPayments() + getTotalFailedPayments();
        if (total == 0) return 0.0;
        return (getTotalSuccessfulPayments() * 100.0) / total;
    }
}
