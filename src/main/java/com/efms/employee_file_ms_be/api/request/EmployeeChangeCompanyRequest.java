package com.efms.employee_file_ms_be.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeChangeCompanyRequest {

    @NotNull(message = "New company ID is required")
    private UUID newCompanyId;

    @NotNull
    private String newCompanyName;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;
}
