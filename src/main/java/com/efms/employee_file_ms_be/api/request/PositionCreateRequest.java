package com.efms.employee_file_ms_be.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class PositionCreateRequest {

    @NotBlank
    private String name;

    private String description;

    private String departmentId;
}