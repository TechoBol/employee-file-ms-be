package com.efms.employee_file_ms_be.api.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class GeneralSettingsUpdateRequest {

    private Integer workingDaysPerMonth;

    @DecimalMin(value = "0.0", message = "Seniority increase percentage must be positive")
    @DecimalMax(value = "100.0", message = "Seniority increase percentage cannot exceed 100")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal seniorityIncreasePercentage;

    private BigDecimal contributionAfpPercentage;
}
