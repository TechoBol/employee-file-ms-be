package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Memorandum;
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
public interface MemorandumRepository extends JpaRepository<Memorandum, UUID> {

    Optional<Memorandum> findByIdAndCompanyId(UUID id, UUID companyId);

    @Query("""
        SELECT m FROM Memorandum m
        WHERE m.employee.id = :employeeId
          AND m.companyId = :companyId
          AND m.memorandumDate >= :startDate
          AND m.memorandumDate <= :endDate
        ORDER BY m.memorandumDate DESC
    """)
    List<Memorandum> findByEmployeeAndCompanyInDateRange(
            @Param("employeeId") UUID employeeId,
            @Param("companyId") UUID companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT m FROM Memorandum m
        WHERE m.companyId = :companyId
          AND m.memorandumDate >= :startDate
          AND m.memorandumDate <= :endDate
        ORDER BY m.memorandumDate DESC
    """)
    List<Memorandum> findByCompanyInDateRange(
            @Param("companyId") UUID companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    int deleteByIdAndCompanyId(UUID id, UUID companyId);
}