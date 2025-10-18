package com.efms.employee_file_ms_be.model.domain;

import com.efms.employee_file_ms_be.model.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Entity
@Table(name = Constants.Vacation.NAME)
@Getter
@Setter
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false, foreignKey = @ForeignKey(name = "fk_vacation_employee"))
    private Employee employee;

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer daysTaken;

    @Column(length = 500)
    private String notes;

    @Column
    private Boolean isPaid = true;

    @Column
    private String createdBy;
}
