package com.efms.employee_file_ms_be.api.response.payroll;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
public class PayrollSummaryPageResponse {
    private Page<PayrollEmployeeResponse> payrolls;
    private PayrollSummaryResponse.PayrollTotals totals;

    public static PayrollSummaryPageResponse from(Page<PayrollEmployeeResponse> page) {
        PayrollSummaryPageResponse response = new PayrollSummaryPageResponse();
        response.setPayrolls(page);

        PayrollSummaryResponse summary = PayrollSummaryResponse.from(page.getContent());
        response.setTotals(summary.getTotals());

        return response;
    }
}