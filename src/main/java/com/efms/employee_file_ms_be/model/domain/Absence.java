package com.efms.employee_file_ms_be.model.domain;

import com.efms.employee_file_ms_be.model.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
@Entity
@Table(name = Constants.Absence.NAME)
public class Absence extends Audit {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private AbsenceType type;

    @Column(nullable = false)
    private AbsenceDuration duration;

    @Column(nullable = false)
    private LocalDate date;

    private LocalDate endDate;

    @Column(length = 100)
    private String reason;

    @Column(length = 200)
    private String description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "salary_event_id")
    private SalaryEvent salaryEvent;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollStatus status;
}
