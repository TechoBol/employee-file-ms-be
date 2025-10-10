package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
public interface LocationRepository extends JpaRepository<Location, UUID> {

    Optional<Location> findByIdAndCompanyId(UUID id, UUID companyId);

    List<Location> findAllByBranchCompanyId(UUID companyId);
}
