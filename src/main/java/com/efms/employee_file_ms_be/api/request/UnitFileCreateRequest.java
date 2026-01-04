package com.efms.employee_file_ms_be.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class UnitFileCreateRequest {

    private String originalName;

    private String description;

    private String section;

    private String uuidFileName;
}
