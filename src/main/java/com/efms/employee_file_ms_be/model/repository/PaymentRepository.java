package com.efms.employee_file_ms_be.model.repository;

import com.efms.employee_file_ms_be.model.domain.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID>, JpaSpecificationExecutor<Payment> {

    /**
     * Find all payments for a specific company
     */
    List<Payment> findAllByCompanyId(UUID companyId);

    /**
     * Find payments with pagination and sorting by company
     */
    Page<Payment> findAllByCompanyId(UUID companyId, Pageable pageable);

    long countByPeriod(Integer period);

    long countByCompanyIdAndPeriod(UUID companyId, Integer period);

    List<Payment> findAllByCompanyIdAndEmployeeIdAndPeriod(
            UUID companyId,
            UUID employeeId,
            Integer period
    );

    Page<Payment> findAllByCompanyIdAndPeriod(
            UUID companyId,
            Integer period,
            Pageable pageable
    );

    List<Payment> findAllByCompanyIdAndPeriod(UUID companyId, Integer period);

    @Modifying
    @Transactional
    @Query("DELETE FROM Payment p WHERE p.companyId = :companyId AND p.period = :period")
    int deleteByCompanyIdAndPeriod(@Param("companyId") UUID companyId,
                                   @Param("period") Integer period);

    @Modifying
    @Transactional
    @Query("DELETE FROM Payment p WHERE p.period = :period")
    int deleteByPeriod(@Param("period") Integer period);
}
