package com.efms.employee_file_ms_be.model.domain;

import com.efms.employee_file_ms_be.model.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
@Entity
@Table(name = Constants.GeneralSettings.NAME)
public class GeneralSettings extends Audit {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private Integer workingDaysPerMonth;

    @Column(nullable = false)
    private BigDecimal seniorityIncreasePercentage;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal contributionAfpPercentage;
}
