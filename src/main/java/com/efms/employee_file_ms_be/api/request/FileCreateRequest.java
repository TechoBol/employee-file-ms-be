package com.efms.employee_file_ms_be.api.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class FileCreateRequest {

    private String employeeId;

    private List<UnitFileCreateRequest> unitFiles;
}
