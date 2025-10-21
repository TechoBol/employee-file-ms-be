package com.efms.employee_file_ms_be.api.request;

import com.efms.employee_file_ms_be.model.domain.MemorandumType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class MemorandumUpdateRequest {

    private MemorandumType type;

    private String description;

    private LocalDate memorandumDate;

    private Boolean isPositive;
}
