package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SalaryEventRepository extends JpaRepository<SalaryEvent, UUID> {

    List<SalaryEvent> findByEmployeeCompanyId(UUID companyId);

    List<SalaryEvent> findByEmployeeId(UUID employeeId);
}
