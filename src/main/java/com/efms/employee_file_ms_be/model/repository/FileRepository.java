package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
public interface FileRepository extends JpaRepository<File, UUID> {

    @Query("SELECT f FROM File f WHERE f.employee.id = :employeeId AND f.companyId = :companyId")
    Optional<File> findByEmployeeIdAndCompanyId(UUID employeeId, UUID companyId);
}
