package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    Optional<Department> findByIdAndCompanyId(UUID id, UUID companyId);

    List<Department> findAllByCompanyId(UUID id);
}
