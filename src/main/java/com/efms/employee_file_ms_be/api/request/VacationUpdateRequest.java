package com.efms.employee_file_ms_be.api.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class VacationUpdateRequest {

    private LocalDate startDate;

    private LocalDate endDate;

    private String notes;
}
