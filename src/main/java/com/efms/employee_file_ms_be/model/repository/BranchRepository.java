package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
public interface BranchRepository extends JpaRepository<Branch, UUID> {

    @Query("SELECT b FROM Branch b JOIN b.company c WHERE c.id = :companyId")
    List<Branch> findBranchesByCompanyId(@Param("companyId") UUID companyId);
}
