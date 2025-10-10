package com.efms.employee_file_ms_be.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
public class CompanyUpdateRequest {
    private String name;
    private String type;
}
