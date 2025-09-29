package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Absence;
import com.efms.employee_file_ms_be.model.domain.AbsenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
public interface AbsenceRepository extends JpaRepository<Absence, UUID> {

    List<Absence> findByEmployeeCompanyId(UUID companyId);

    List<Absence> findByEmployeeId(UUID employeeId);

    List<Absence> findByEmployeeIdAndType(UUID employeeId, AbsenceType type);

    List<Absence> findByEmployeeIdAndDateBetween(UUID employeeId, LocalDate startDate, LocalDate endDate);

    List<Absence> findByEmployeeCompanyIdAndDateBetween(UUID companyId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Absence a WHERE a.employee.id = :employeeId " +
            "AND ((a.date <= :date AND (a.endDate IS NULL OR a.endDate >= :date)) " +
            "OR (a.date <= :endDate AND (a.endDate IS NULL OR a.endDate >= :startDate)))")
    List<Absence> findConflictingAbsences(@Param("employeeId") UUID employeeId,
                                          @Param("date") LocalDate date,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
}
