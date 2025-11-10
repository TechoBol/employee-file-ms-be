package com.efms.employee_file_ms_be.command.payment;

import com.efms.employee_file_ms_be.api.response.payment.PaymentProcessSummary;
import com.efms.employee_file_ms_be.api.response.payment.PaymentReprocessSummary;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Payment;
import com.efms.employee_file_ms_be.model.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@CommandExecute
@RequiredArgsConstructor
@Slf4j
public class PaymentReprocessCmd implements Command {

    @Setter
    private Integer period;

    private UUID companyId;

    @Getter
    private PaymentReprocessSummary summary;

    private final PaymentRepository paymentRepository;
    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        log.info("=== Starting payment reprocessing - Period: {}, CompanyId: {} ===",
                period, companyId);

        validateInputs();

        long startTime = System.currentTimeMillis();
        summary = new PaymentReprocessSummary();
        summary.setPeriod(period);
        summary.setCompanyId(companyId);
        summary.setStartTime(LocalDateTime.now());

        try {
            deleteExistingPayments();
            reprocessPayments();
            summary.setSuccess(true);
        } catch (Exception e) {
            summary.setSuccess(false);
            summary.setErrorMessage(e.getMessage());
            log.error("Error during payment reprocessing: {}", e.getMessage(), e);
            throw new RuntimeException("Payment reprocessing failed", e);
        }

        summary.setEndTime(LocalDateTime.now());
        summary.setDurationMs(System.currentTimeMillis() - startTime);

        logFinalSummary();
    }

    private void validateInputs() {
        if (period == null) {
            throw new IllegalArgumentException("Period cannot be null");
        }
    }

    @Transactional
    protected void deleteExistingPayments() {
        log.info("Deleting existing payments for period {}{}",
                period, companyId != null ? " and company " + companyId : "");

        long countBefore = countExistingPayments();
        summary.setDeletedPayments(countBefore);

        if (countBefore == 0) {
            log.warn("No existing payments found for period {}{}",
                    period, companyId != null ? " and company " + companyId : "");
            return;
        }

        if (companyId != null) {
            deleteByCompanyAndPeriod(companyId, period);
        } else {
            deleteByPeriod(period);
        }

        log.info("Deleted {} payments", countBefore);
    }

    private long countExistingPayments() {
        if (companyId != null) {
            return paymentRepository.findAllByCompanyIdAndEmployeeIdAndPeriod(
                    companyId, null, period).size();
        } else {
            return paymentRepository.countByPeriod(period);
        }
    }

    private void deleteByCompanyAndPeriod(UUID companyId, Integer period) {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Payment> page;

        do {
            page = paymentRepository.findAllByCompanyIdAndPeriod(companyId, period, pageable);
            if (!page.isEmpty()) {
                paymentRepository.deleteAll(page.getContent());
                log.debug("Deleted batch of {} payments", page.getNumberOfElements());
            }
            pageable = pageable.next();
        } while (page.hasNext());
    }

    private void deleteByPeriod(Integer period) {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Payment> page;

        do {
            List<Payment> payments = paymentRepository.findAll().stream()
                    .filter(p -> p.getPeriod().equals(period))
                    .limit(100)
                    .toList();

            if (!payments.isEmpty()) {
                paymentRepository.deleteAll(payments);
                log.debug("Deleted batch of {} payments", payments.size());
            }

            long remaining = paymentRepository.countByPeriod(period);
            if (remaining == 0) break;

        } while (true);
    }

    private void reprocessPayments() {
        log.info("Starting payment reprocessing...");

        if (companyId != null) {
            reprocessForSingleCompany();
        } else {
            reprocessForAllCompanies();
        }
    }

    private void reprocessForSingleCompany() {
        PaymentProcessSingleCompanyCmd processCmd = commandFactory.createCommand(PaymentProcessSingleCompanyCmd.class);
        processCmd.setPeriod(period);
        processCmd.execute();

        PaymentProcessSummary processSummary = processCmd.getSummary();
        summary.setNewPaymentsCreated(processSummary.getTotalSuccessfulPayments());
        summary.setFailedPayments(processSummary.getTotalFailedPayments());
        summary.setTotalEmployees(processSummary.getTotalEmployees());
    }

    private void reprocessForAllCompanies() {
        PaymentProcessAllCompaniesCmd processCmd = commandFactory.createCommand(PaymentProcessAllCompaniesCmd.class);
        processCmd.setPeriod(period);
        processCmd.execute();

        PaymentProcessSummary processSummary = processCmd.getSummary();
        summary.setNewPaymentsCreated(processSummary.getTotalSuccessfulPayments());
        summary.setFailedPayments(processSummary.getTotalFailedPayments());
        summary.setTotalEmployees(processSummary.getTotalEmployees());
    }

    private void logFinalSummary() {
        log.info("=== Payment Reprocessing Completed ===");
        log.info("Period: {}", summary.getPeriod());
        log.info("Company: {}", summary.getCompanyId() != null ? summary.getCompanyId() : "ALL");
        log.info("Duration: {} ms ({} seconds)", summary.getDurationMs(), summary.getDurationMs() / 1000.0);
        log.info("Deleted payments: {}", summary.getDeletedPayments());
        log.info("New payments created: {}", summary.getNewPaymentsCreated());
        log.info("Failed payments: {}", summary.getFailedPayments());
        log.info("Total employees processed: {}", summary.getTotalEmployees());
        log.info("Success: {}", summary.isSuccess());

        if (summary.getFailedPayments() > 0) {
            log.warn("There were {} failed payments during reprocessing. Review the logs for details.",
                    summary.getFailedPayments());
        }
    }
}
