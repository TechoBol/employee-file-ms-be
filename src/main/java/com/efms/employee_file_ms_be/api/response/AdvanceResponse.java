package com.efms.employee_file_ms_be.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
@Builder
public class AdvanceResponse {

    private UUID id;

    private String employeeId;

    private BigDecimal amount;

    private LocalDate advanceDate;
}