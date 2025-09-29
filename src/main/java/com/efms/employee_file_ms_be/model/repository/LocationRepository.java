package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
public interface LocationRepository extends JpaRepository<Location, UUID> {

    @Query("""
    SELECT l.id as id, l.name as name, b.id as branchId, b.name as branchName
    FROM Location l\s
    JOIN l.branch b\s
    WHERE b.company.id = :companyId
   \s""")
    List<Location> findLocationsByCompanyId(@Param("companyId") UUID companyId);
}
