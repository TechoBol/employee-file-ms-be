package com.efms.employee_file_ms_be.api.request;

import com.efms.employee_file_ms_be.model.domain.EmployeeStatus;
import com.efms.employee_file_ms_be.model.domain.EmployeeType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
@Builder
public class EmployeeSearchRequest {
    private String search;
    private String ci;
    private String email;
    private String phone;
    private EmployeeType type;
    private EmployeeStatus status;
    private Boolean isDisassociated;
    private UUID branchId;
    private UUID positionId;
    private UUID companyId;
    private Pageable pageable;
}
