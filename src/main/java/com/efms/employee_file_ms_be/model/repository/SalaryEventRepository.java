package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.domain.SalaryEventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalaryEventRepository extends JpaRepository<SalaryEvent, UUID> {

    Page<SalaryEvent> findAllByCompanyId(UUID companyId, Pageable pageable);

    @Query("""
        SELECT s FROM SalaryEvent s\s
        WHERE s.companyId = :companyId
          AND (s.startDate <= :endDate AND (s.endDate IS NULL OR s.endDate >= :startDate))
   \s""")
    List<SalaryEvent> findByCompanyInDateRange(
            @Param("companyId") UUID companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    Optional<SalaryEvent> findByIdAndCompanyId(UUID id, UUID companyId);

    @Query("""
        SELECT s FROM SalaryEvent s
        WHERE s.employee.id = :employeeId
          AND s.companyId = :companyId
          AND (:category IS NULL OR s.category = :category)
          AND (s.startDate <= :endDate AND (s.endDate IS NULL OR s.endDate >= :startDate))
    """)
    List<SalaryEvent> findByEmployeeAndCompanyAndOptionalCategoryInDateRange(
            @Param("employeeId") UUID employeeId,
            @Param("companyId") UUID companyId,
            @Param("category") SalaryEventCategory category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
