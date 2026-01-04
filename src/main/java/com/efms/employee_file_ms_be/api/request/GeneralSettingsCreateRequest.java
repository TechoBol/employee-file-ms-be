package com.efms.employee_file_ms_be.api.request;

import com.efms.employee_file_ms_be.api.Constants;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralSettingsCreateRequest {

    @NotNull
    private Integer workingDaysPerMonth;

    @NotNull
    @DecimalMin(value = "0.0", message = Constants.ErrorMessage.SENIORITY_MIN_PERCENTAGE)
    @DecimalMax(value = "100.0", message = Constants.ErrorMessage.SENIORITY_MAX_PERCENTAGE)
    @Digits(integer = 3, fraction = 2)
    private BigDecimal seniorityIncreasePercentage;

    private BigDecimal contributionAfpPercentage;
}
