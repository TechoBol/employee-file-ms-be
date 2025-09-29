package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Company;
import com.efms.employee_file_ms_be.model.repository.projection.CompanyProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query("SELECT c.id as id, c.name as name FROM Company c")
    List<CompanyProjection> findAllCompaniesProjection();
}
