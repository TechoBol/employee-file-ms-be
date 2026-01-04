package com.efms.employee_file_ms_be.api.request;

import com.efms.employee_file_ms_be.model.domain.AbsenceDuration;
import com.efms.employee_file_ms_be.model.domain.AbsenceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class AbsenceCreateRequest {

    @NotBlank
    private String employeeId;

    @NotNull
    private AbsenceType type;

    @NotNull
    private AbsenceDuration duration;

    @NotNull
    private LocalDate date;

    private LocalDate endDate;

    @Size(max = 100)
    private String reason;

    @Size(max = 200)
    private String description;
}
