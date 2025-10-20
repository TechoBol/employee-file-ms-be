package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Absence;
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

    Page<Absence> findAllByCompanyId(UUID companyId, Pageable pageable);

    Optional<Absence> findByIdAndCompanyId(UUID id, UUID companyId);

    @Query("""
        SELECT a FROM Absence a
        WHERE a.employee.id = :employeeId
          AND a.companyId = :companyId
          AND a.date >= :startDate
          AND a.date <= :endDate
        ORDER BY a.date DESC
    """)
    List<Absence> findByEmployeeAndCompanyInDateRange(
            @Param("employeeId") UUID employeeId,
            @Param("companyId") UUID companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT a FROM Absence a
        WHERE a.companyId = :companyId
          AND a.date >= :startDate
          AND a.date <= :endDate
        ORDER BY a.date DESC
    """)
    List<Absence> findByCompanyInDateRange(
            @Param("companyId") UUID companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
