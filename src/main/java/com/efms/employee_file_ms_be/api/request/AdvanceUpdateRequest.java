package com.efms.employee_file_ms_be.api.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class AdvanceUpdateRequest {

    private BigDecimal percentageAmount;

    private LocalDate advanceDate;
}
