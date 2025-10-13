package com.efms.employee_file_ms_be.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class DepartmentUpdateRequest {

    private String name;
    private String description;
}
