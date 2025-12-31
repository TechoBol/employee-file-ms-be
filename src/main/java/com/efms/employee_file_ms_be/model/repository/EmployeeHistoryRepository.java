package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.ChangeType;
import com.efms.employee_file_ms_be.model.domain.EmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Repository
public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, UUID> {

    Page<EmployeeHistory> findByEmployeeIdAndCompanyIdOrderByChangedAtDesc(
            UUID employeeId,
            UUID companyId,
            Pageable pageable
    );

    @Query("SELECT h FROM EmployeeHistory h WHERE h.employeeId = :employeeId " +
            "AND h.companyId = :companyId AND h.changeType = :changeType " +
            "ORDER BY h.changedAt DESC")
    Page<EmployeeHistory> findByEmployeeIdAndCompanyIdAndChangeType(
            @Param("employeeId") UUID employeeId,
            @Param("companyId") UUID companyId,
            @Param("changeType") ChangeType changeType,
            Pageable pageable
    );

    @Query("SELECT h FROM EmployeeHistory h WHERE h.employeeId = :employeeId " +
            "AND h.companyId = :companyId " +
            "AND h.changedAt BETWEEN :startDate AND :endDate " +
            "ORDER BY h.changedAt DESC")
    Page<EmployeeHistory> findByEmployeeIdAndCompanyIdAndDateRange(
            @Param("employeeId") UUID employeeId,
            @Param("companyId") UUID companyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("SELECT h FROM EmployeeHistory h WHERE h.employeeId = :employeeId " +
            "AND h.companyId = :companyId AND h.changeType = :changeType " +
            "AND h.changedAt BETWEEN :startDate AND :endDate " +
            "ORDER BY h.changedAt DESC")
    Page<EmployeeHistory> findByEmployeeIdAndCompanyIdAndChangeTypeAndDateRange(
            @Param("employeeId") UUID employeeId,
            @Param("companyId") UUID companyId,
            @Param("changeType") ChangeType changeType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}