package com.efms.employee_file_ms_be.api.request;

import com.efms.employee_file_ms_be.api.Constants;
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

    @DecimalMin(value = "0.0", message = Constants.ErrorMessage.SENIORITY_MIN_PERCENTAGE)
    @DecimalMax(value = "100.0", message = Constants.ErrorMessage.SENIORITY_MAX_PERCENTAGE)
    @Digits(integer = 3, fraction = 2)
    private BigDecimal seniorityIncreasePercentage;

    private BigDecimal contributionAfpPercentage;
}
