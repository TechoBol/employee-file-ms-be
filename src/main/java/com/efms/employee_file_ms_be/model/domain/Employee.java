package com.efms.employee_file_ms_be.model.domain;

import com.efms.employee_file_ms_be.model.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Constants.EmployeeTable.NAME)
@SQLRestriction("is_deleted = false")
public class Employee extends Audit {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String firstName;

    @Column(nullable = false, length = 150)
    private String lastName;

    @Column(nullable = false, length = 15, unique = true)
    private String ci;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 180)
    private String address;

    private LocalDate birthDate;

    private LocalDate hireDate;

    @Column(nullable = false)
    private EmployeeStatus status;

    @Column(nullable = false)
    private EmployeeType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private EmergencyContact emergencyContact;

    @Column(nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    @Column
    private LocalDateTime deletedAt;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private BaseSalary baseSalary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memorandum> memorandums;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;
}
