package com.efms.employee_file_ms_be.api.response.payment;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class PaymentByBranchResponse {
    private UUID branchId;
    private String branchName;
    private Integer employeeCount;
    private List<PaymentEmployeeResponse> payments;
    private PaymentSummaryResponse.PaymentTotals totals;
}
