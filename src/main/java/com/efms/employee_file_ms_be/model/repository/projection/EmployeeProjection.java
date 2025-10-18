package com.efms.employee_file_ms_be.model.repository.projection;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
public interface EmployeeProjection {
    UUID getId();
    String getFirstName();
    String getLastName();
    String getCi();
    String getEmail();
    UUID getPositionId();
    UUID getBranchId();
}