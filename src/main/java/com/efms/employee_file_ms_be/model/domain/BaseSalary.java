package com.efms.employee_file_ms_be.model.domain;

import com.efms.employee_file_ms_be.model.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
@Entity
@Table(name = Constants.BaseSalaryTable.NAME)
public class BaseSalary extends Audit {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(
            precision = Constants.TypeSpecifications.NUMERIC_PRECISION,
            scale = Constants.TypeSpecifications.NUMERIC_SCALE,
            nullable = false
    )
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, columnDefinition = "uuid")
    private Employee employee;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;
}
