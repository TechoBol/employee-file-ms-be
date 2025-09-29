package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.BaseSalary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BaseSalaryRepository extends JpaRepository<BaseSalary, UUID> {
    @EntityGraph(attributePaths = {
            "employee",
    })
    @Query("SELECT bs FROM BaseSalary bs WHERE bs.employee.id = :employeeId")
    Optional<BaseSalary> findByEmployeeId(@Param("employeeId") UUID employeeId);

    @Query("""
    SELECT bs FROM BaseSalary bs\s
    JOIN bs.employee e\s
    WHERE e.company.id = :companyId\s
    AND e.isDeleted = false
   \s""")
    Page<BaseSalary> findAllByCompanyIdAndActiveEmployees(@Param("companyId") UUID companyId, Pageable pageable);
}
