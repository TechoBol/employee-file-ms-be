package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.GeneralSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
public interface GeneralSettingsRepository extends JpaRepository<GeneralSettings, UUID> {

    Optional<GeneralSettings> findByIdAndCompanyId(UUID id, UUID companyId);

    Optional<GeneralSettings> findByCompanyId(UUID companyId);
}
