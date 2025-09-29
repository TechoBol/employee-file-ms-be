package com.efms.employee_file_ms_be.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
@Builder
public class BranchResponse {

    private String id;

    private String name;

    private String companyId;

//    private String companyName;
}
