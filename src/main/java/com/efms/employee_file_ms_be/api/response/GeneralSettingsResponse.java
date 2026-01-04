package com.efms.employee_file_ms_be.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
@Builder
public class GeneralSettingsResponse {
    private String id;
    private String companyId;
    private Integer workingDaysPerMonth;
    private BigDecimal seniorityIncreasePercentage;
    private BigDecimal contributionAfpPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
