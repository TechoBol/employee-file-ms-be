package com.efms.employee_file_ms_be.model.domain;

import com.efms.employee_file_ms_be.model.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
@Entity
@Table(name = Constants.Payment.NAME)
public class Payment {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    private Integer period;

    private LocalDateTime payment_date;

    @Column(
            precision = Constants.TypeSpecifications.NUMERIC_PRECISION,
            scale = Constants.TypeSpecifications.NUMERIC_SCALE,
            nullable = false
    )
    private BigDecimal grossAmount;

    @Column(
            precision = Constants.TypeSpecifications.NUMERIC_PRECISION,
            scale = Constants.TypeSpecifications.NUMERIC_SCALE,
            nullable = false
    )
    private BigDecimal totalDeductions;

    @Column(
            precision = Constants.TypeSpecifications.NUMERIC_PRECISION,
            scale = Constants.TypeSpecifications.NUMERIC_SCALE,
            nullable = false
    )
    private BigDecimal netAmount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
