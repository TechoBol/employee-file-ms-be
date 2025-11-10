package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Absence;
import com.efms.employee_file_ms_be.model.domain.PayrollStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
public interface AbsenceRepository extends JpaRepository<Absence, UUID> {

    @Query("""
        SELECT a FROM Absence a
        WHERE a.companyId = :companyId
          AND (:status IS NULL OR a.status = :status)
    """)
    Page<Absence> findAllByCompanyId(
            @Param("companyId") UUID companyId,
            @Param("status") PayrollStatus status,
            Pageable pageable
    );

    @Query("""
        SELECT a FROM Absence a
        WHERE a.id = :id
          AND a.companyId = :companyId
          AND (:status IS NULL OR a.status = :status)
    """)
    Optional<Absence> findByIdAndCompanyId(
            @Param("id") UUID id,
            @Param("companyId") UUID companyId,
            @Param("status") PayrollStatus status
    );

    @Query("""
        SELECT a FROM Absence a
        WHERE a.employee.id = :employeeId
          AND a.companyId = :companyId
          AND (:status IS NULL OR a.status = :status)
          AND a.date >= :startDate
          AND a.date <= :endDate
        ORDER BY a.date DESC
    """)
    List<Absence> findByEmployeeAndCompanyInDateRange(
            @Param("employeeId") UUID employeeId,
            @Param("companyId") UUID companyId,
            @Param("status") PayrollStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT a FROM Absence a
        WHERE a.companyId = :companyId
          AND (:status IS NULL OR a.status = :status)
          AND a.date <= :endDate
          AND (a.endDate IS NULL OR a.endDate >= :startDate)
        ORDER BY a.date DESC
    """)
    List<Absence> findByCompanyInDateRange(
            @Param("companyId") UUID companyId,
            @Param("status") PayrollStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    int deleteByIdAndCompanyId(UUID id, UUID companyId);
}
