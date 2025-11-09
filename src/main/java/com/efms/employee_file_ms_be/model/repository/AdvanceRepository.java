package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Advance;
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
public interface AdvanceRepository extends JpaRepository<Advance, UUID> {

    Page<Advance> findAllByCompanyId(UUID companyId, Pageable pageable);

    Optional<Advance> findByIdAndCompanyId(UUID id, UUID companyId);

    @Query("""
        SELECT a FROM Advance a
        WHERE a.employee.id = :employeeId
          AND a.companyId = :companyId
          AND a.advanceDate >= :startDate
          AND a.advanceDate <= :endDate
        ORDER BY a.advanceDate DESC
    """)
    List<Advance> findByEmployeeAndCompanyInDateRange(
            @Param("employeeId") UUID employeeId,
            @Param("companyId") UUID companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT a FROM Advance a
        WHERE a.companyId = :companyId
          AND a.advanceDate >= :startDate
          AND a.advanceDate <= :endDate
        ORDER BY a.advanceDate DESC
    """)
    List<Advance> findByCompanyInDateRange(
            @Param("companyId") UUID companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    int deleteByIdAndCompanyId(UUID id, UUID companyId);
}
