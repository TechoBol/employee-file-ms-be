package com.efms.employee_file_ms_be.api.request;

import com.efms.employee_file_ms_be.model.domain.MemorandumType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class MemorandumCreateRequest {

    @NotBlank
    private String employeeId;

    @NotNull
    private MemorandumType type;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate memorandumDate;

    @NotNull
    private Boolean isPositive;
}
