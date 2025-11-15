package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.repository.projection.EmployeeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @EntityGraph(attributePaths = {"position", "position.department", "branch"})
    Optional<Employee> findByIdAndCompanyId(UUID id, UUID companyId);

    @EntityGraph(attributePaths = {"position", "position.department", "branch"})
    Page<Employee> findAllByCompanyId(UUID companyId, Pageable pageable);

    @Query("""
        SELECT e.id as id,
               e.firstName as firstName,
               e.lastName as lastName,
               e.ci as ci,
               e.email as email,
               e.position.id as positionId,
               e.branch.id as branchId,
               e.type as type
        FROM Employee e
        WHERE e.companyId = :companyId
        AND e.baseSalary IS NOT NULL
    """)
    Page<EmployeeProjection> findAllProjectedByCompanyId(
            @Param("companyId") UUID companyId,
            Pageable pageable
    );

    Page<Employee> findAllByCompanyIdAndIsDisassociatedAndDisassociatedAtBefore(UUID companyId, boolean disassociated, LocalDateTime date, Pageable pageable);
}
