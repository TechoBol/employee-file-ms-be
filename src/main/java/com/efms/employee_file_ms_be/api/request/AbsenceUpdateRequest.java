package com.efms.employee_file_ms_be.api.request;

import com.efms.employee_file_ms_be.model.domain.AbsenceDuration;
import com.efms.employee_file_ms_be.model.domain.AbsenceType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class AbsenceUpdateRequest {

    private AbsenceType type;

    private AbsenceDuration duration;

    private LocalDate date;

    private LocalDate endDate;

    @Size(max = 100)
    private String reason;

    @Size(max = 250)
    private String description;
}