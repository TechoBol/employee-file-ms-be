package com.efms.employee_file_ms_be.api.response;

import com.efms.employee_file_ms_be.model.domain.EmployeeSnapshot;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeHistoryResponse {
    private String id;
    private String employeeId;
    private LocalDateTime changedAt;
    private String changedBy;
    private String changeType;
    private String reason;
    private EmployeeSnapshot snapshot;
    private Map<String, FieldChangeResponse> changes;
    private String companyId;
}
