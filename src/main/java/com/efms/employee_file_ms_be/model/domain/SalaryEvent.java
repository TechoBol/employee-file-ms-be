package com.efms.employee_file_ms_be.model.domain;

import com.efms.employee_file_ms_be.model.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = Constants.SalaryEvent.NAME)
@SQLRestriction("status = 'OPEN'")
public class SalaryEvent {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private SalaryEventType type;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private SalaryEventCategory category = SalaryEventCategory.MANUAL;

    @Column(length = 150)
    private String description;

    @Column(
            precision = Constants.TypeSpecifications.NUMERIC_PRECISION,
            scale = Constants.TypeSpecifications.NUMERIC_SCALE,
            nullable = false
    )
    private BigDecimal amount;

    @Column(length = 10, nullable = false)
    private SalaryEventFrequency frequency = SalaryEventFrequency.ONE_TIME;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private PayrollStatus status;
}
