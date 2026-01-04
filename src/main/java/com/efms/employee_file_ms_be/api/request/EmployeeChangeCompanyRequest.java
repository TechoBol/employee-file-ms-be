package com.efms.employee_file_ms_be.api.request;

import com.efms.employee_file_ms_be.api.Constants;
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

    @NotNull(message = Constants.ErrorMessage.NEW_COMPANY_ID_REQUIRED)
    private UUID newCompanyId;

    @NotNull
    private String newCompanyName;

    @Size(max = 500, message = Constants.ErrorMessage.REASON_MAX_CHARS)
    private String reason;
}
