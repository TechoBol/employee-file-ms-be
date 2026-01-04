package com.efms.employee_file_ms_be.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String type;
}
