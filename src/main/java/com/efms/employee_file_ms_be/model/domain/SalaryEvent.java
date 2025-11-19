package com.efms.employee_file_ms_be.model.domain;

import com.efms.employee_file_ms_be.model.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = Constants.SalaryEvent.NAME)
public class SalaryEvent {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalaryEventType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SalaryEventCategory category = SalaryEventCategory.MANUAL;

    @Column(length = 250)
    private String description;

    @Column(
            precision = Constants.TypeSpecifications.NUMERIC_PRECISION,
            scale = Constants.TypeSpecifications.NUMERIC_SCALE,
            nullable = false
    )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private SalaryEventFrequency frequency = SalaryEventFrequency.ONE_TIME;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollStatus status;

    @OneToOne(mappedBy = "salaryEvent")
    private Absence absence;

    @OneToOne(mappedBy = "salaryEvent")
    private Advance advance;
}
