package com.efms.employee_file_ms_be.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
@Builder
public class VacationResponse {

    private UUID id;

    private String employeeId;

    private LocalDate startDate;

    private LocalDate endDate;

    private int daysTaken;

    private String notes;
}
