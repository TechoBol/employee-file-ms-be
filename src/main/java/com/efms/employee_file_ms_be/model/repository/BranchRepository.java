package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
public interface BranchRepository extends JpaRepository<Branch, UUID> {

    List<Branch> findByCompanyId(UUID companyId);

    Optional<Branch> findByIdAndCompanyId(UUID id, UUID companyId);
}
