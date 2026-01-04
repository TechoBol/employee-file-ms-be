package com.efms.employee_file_ms_be.api.response.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Josue Veliz
 */
@Getter
@Setter
@Builder
public class PaymentDeductionResponse {

    private String type;

    private Integer qty;

    private BigDecimal totalDeduction;
}