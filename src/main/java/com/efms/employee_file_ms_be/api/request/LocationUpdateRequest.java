package com.efms.employee_file_ms_be.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class LocationUpdateRequest {

    private String name;

    private String branchId;
}
