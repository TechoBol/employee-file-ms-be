package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    /**
     * Find all payments for a specific company
     */
    List<Payment> findAllByCompanyId(UUID companyId);

    /**
     * Find payments with pagination and sorting by company
     */
    Page<Payment> findAllByCompanyId(UUID companyId, Pageable pageable);

    long countByPeriod(Integer period);

    List<Payment> findAllByCompanyIdAndEmployeeIdAndPeriod(
            UUID companyId,
            UUID employeeId,
            Integer period
    );

    @EntityGraph(attributePaths = {"employee"})
    Page<Payment> findAllByCompanyIdAndPeriod(
            UUID companyId,
            Integer period,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"employee"})
    List<Payment> findAllByCompanyIdAndPeriod(UUID companyId, Integer period);
}
