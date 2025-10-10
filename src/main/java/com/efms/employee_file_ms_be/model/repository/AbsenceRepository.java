package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Absence;
import com.efms.employee_file_ms_be.model.domain.AbsenceType;
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

    Optional<Absence> findByIdAndCompanyId(UUID id, UUID companyId);

    List<Absence> findByCompanyId(UUID companyId);

    List<Absence> findByEmployeeIdAndCompanyId(UUID employeeId, UUID companyId);

    List<Absence> findByEmployeeIdAndCompanyIdAndType(UUID employeeId, UUID companyId, AbsenceType type);

    List<Absence> findByCompanyIdAndEmployeeIdAndDateBetween(UUID companyId, UUID employeeId, LocalDate date, LocalDate date2);

    List<Absence> findByCompanyIdAndDateBetween(UUID companyId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Absence a WHERE a.employee.id = :employeeId " +
            "AND ((a.date <= :date AND (a.endDate IS NULL OR a.endDate >= :date)) " +
            "OR (a.date <= :endDate AND (a.endDate IS NULL OR a.endDate >= :startDate)))")
    List<Absence> findConflictingAbsences(@Param("employeeId") UUID employeeId,
                                          @Param("date") LocalDate date,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
}
