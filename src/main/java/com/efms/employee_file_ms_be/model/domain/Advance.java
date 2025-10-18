package com.efms.employee_file_ms_be.model.domain;

import com.efms.employee_file_ms_be.model.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Auditable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
@Entity
@Table(name = Constants.Advance.NAME)
public class Advance extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentageAmount;

    @Column(nullable = false)
    private LocalDate advanceDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_event_id")
    private SalaryEvent salaryEvent;

    @Column
    private String createdBy;
}
