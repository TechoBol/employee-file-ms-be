package com.efms.employee_file_ms_be.scheduler;

import com.efms.employee_file_ms_be.api.response.payment.PaymentProcessSummary;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.payment.PaymentProcessAllCompaniesCmd;
import com.efms.employee_file_ms_be.model.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Josue Veliz
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FlexiblePaymentScheduler {

    private final CommandFactory commandFactory;
    private final PaymentRepository paymentRepository;

    @Value("${scheduler.payment.auto-process:true}")
    private boolean autoProcessEnabled;

    /**
     * Main scheduled task
     */
    @Scheduled(cron = "${efms.scheduler.payment.process.cron}")
    public void scheduledPaymentProcess() {
        if (!autoProcessEnabled) {
            log.info("Automatic payment processing is disabled");
            return;
        }

        Integer period = calculatePreviousMonthPeriod();
        processPayments(period, false);
    }

    /**
     * Process payments for a specific period
     * Can be called manually via REST endpoint
     * IMPORTANT: Does NOT delete existing payments.
     * Skips employees that already have a payment for the period.
     */
    public PaymentProcessSummary processPayments(Integer period, boolean skipExisting) {
        log.info("Processing payments for period: {} (skip existing: {})", period, skipExisting);

        if (skipExisting && isPeriodAlreadyProcessed(period)) {
            log.warn("Period {} was already processed. Skipping completely...", period);
            return createSkippedSummary(period);
        }

        log.info("Processing payments - existing payments will be preserved");

        PaymentProcessAllCompaniesCmd command = commandFactory.createCommand(
                PaymentProcessAllCompaniesCmd.class
        );
        command.setPeriod(period);
        command.execute();

        return command.getSummary();
    }

    private Integer calculatePreviousMonthPeriod() {
        LocalDate previousMonth = LocalDate.now().minusMonths(1);
        return previousMonth.getYear() * 100 + previousMonth.getMonthValue();
    }

    private boolean isPeriodAlreadyProcessed(Integer period) {
        long count = paymentRepository.countByPeriod(period);
        return count > 0;
    }

    private PaymentProcessSummary createSkippedSummary(Integer period) {
        PaymentProcessSummary summary = new PaymentProcessSummary();
        summary.setPeriod(period);
        summary.setStartTime(LocalDateTime.now());
        summary.setEndTime(LocalDateTime.now());
        summary.setDurationMs(0L);
        return summary;
    }
}
