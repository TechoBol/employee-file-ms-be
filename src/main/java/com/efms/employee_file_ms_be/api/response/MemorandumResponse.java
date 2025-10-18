package com.efms.employee_file_ms_be.api.response;

import com.efms.employee_file_ms_be.model.domain.MemorandumType;
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
public class MemorandumResponse {

    private UUID id;

    private String employeeId;

    private MemorandumType type;

    private String description;

    private LocalDate memorandumDate;

    private Boolean isPositive;
}
