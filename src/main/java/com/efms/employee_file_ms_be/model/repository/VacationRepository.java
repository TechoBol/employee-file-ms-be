package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Vacation;
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
public interface VacationRepository extends JpaRepository<Vacation, UUID> {

    Optional<Vacation> findByIdAndCompanyId(UUID id, UUID companyId);

    @Query("""
        SELECT v FROM Vacation v\s
        WHERE v.employee.id = :employeeId\s
          AND v.companyId = :companyId
          AND v.startDate <= :endDate\s
          AND v.endDate >= :startDate
        ORDER BY v.startDate
   \s""")
    List<Vacation> findByEmployeeAndCompanyInDateRange(
            @Param("employeeId") UUID employeeId,
            @Param("companyId") UUID companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT v FROM Vacation v\s
        WHERE v.companyId = :companyId
          AND v.startDate <= :endDate\s
          AND v.endDate >= :startDate
        ORDER BY v.startDate
   \s""")
    List<Vacation> findByCompanyInDateRange(
            @Param("companyId") UUID companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
