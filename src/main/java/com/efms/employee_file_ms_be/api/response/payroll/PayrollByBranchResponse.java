package com.efms.employee_file_ms_be.api.response.payroll;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PayrollByBranchResponse {
    private UUID branchId;
    private String branchName;
    private Integer employeeCount;
    private List<PayrollEmployeeResponse> payrolls;
    private PayrollSummaryResponse.PayrollTotals totals;
}
