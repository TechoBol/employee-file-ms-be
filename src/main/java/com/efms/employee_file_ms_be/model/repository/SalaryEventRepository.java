package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.PayrollStatus;
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

    @Query("""
        SELECT s FROM SalaryEvent s
        WHERE s.companyId = :companyId
          AND (:status IS NULL OR s.status = :status)
    """)
    Page<SalaryEvent> findAllByCompanyId(
            @Param("companyId") UUID companyId,
            @Param("status") PayrollStatus status,
            Pageable pageable
    );

    @Query("""
        SELECT s FROM SalaryEvent s
        WHERE s.id = :id
          AND s.companyId = :companyId
          AND (:status IS NULL OR s.status = :status)
    """)
    Optional<SalaryEvent> findByIdAndCompanyId(
            @Param("id") UUID id,
            @Param("companyId") UUID companyId,
            @Param("status") PayrollStatus status
    );

    @Query("""
        SELECT s FROM SalaryEvent s
        WHERE s.companyId = :companyId
          AND (:status IS NULL OR s.status = :status)
          AND (s.startDate <= :endDate AND (s.endDate IS NULL OR s.endDate >= :startDate))
    """)
    List<SalaryEvent> findByCompanyInDateRange(
            @Param("companyId") UUID companyId,
            @Param("status") PayrollStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT s FROM SalaryEvent s
        WHERE s.employee.id = :employeeId
          AND s.companyId = :companyId
          AND (:status IS NULL OR s.status = :status)
          AND (:category IS NULL OR s.category = :category)
          AND s.startDate <= :endDate
          AND (s.endDate IS NULL AND s.startDate >= :startDate 
               OR s.endDate IS NOT NULL AND s.endDate >= :startDate)
    """)
    List<SalaryEvent> findByEmployeeAndCompanyAndOptionalCategoryInDateRange(
            @Param("employeeId") UUID employeeId,
            @Param("companyId") UUID companyId,
            @Param("status") PayrollStatus status,
            @Param("category") SalaryEventCategory category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    int deleteByIdAndCompanyId(UUID id, UUID companyId);
}
