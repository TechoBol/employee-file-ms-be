package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PositionRepository extends JpaRepository<Position, UUID> {

    List<Position> findAllByCompanyId(UUID companyId);

    Optional<Position> findByIdAndCompanyId(UUID id, UUID companyId);

    int deleteByIdAndCompanyId(UUID id, UUID companyId);
}
