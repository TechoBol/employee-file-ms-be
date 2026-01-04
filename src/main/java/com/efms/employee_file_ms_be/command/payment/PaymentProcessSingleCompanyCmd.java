package com.efms.employee_file_ms_be.command.payment;

import com.efms.employee_file_ms_be.api.response.CompanyResponse;
import com.efms.employee_file_ms_be.api.response.payment.CompanyProcessResult;
import com.efms.employee_file_ms_be.api.response.payment.PaymentProcessSummary;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollDeductionResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollEmployeeResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollResponse;
import com.efms.employee_file_ms_be.command.absence.AbsenceProcessCmd;
import com.efms.employee_file_ms_be.command.advance.AdvanceProcessCmd;
import com.efms.employee_file_ms_be.command.company.CompanyGetCmd;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.payroll.PayrollCalculateByCompanyCmd;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventProcessCmd;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.*;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CommandExecute
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessSingleCompanyCmd implements Command {

    @Setter
    private Integer period;

    @Getter
    private PaymentProcessSummary summary;

    private UUID companyId;

    private final CommandFactory commandFactory;
    private final PaymentRepository paymentRepository;
    private final EmployeeRepository employeeRepository;

    private static final int EMPLOYEE_PAGE_SIZE = 50;

    @Override
    public void execute() {
        companyId = UUID.fromString(TenantContext.getTenantId());
        log.info("=== Starting payment processing for company {} - Period: {} ===", companyId, period);

        validateInputs();

        long startTime = System.currentTimeMillis();

        summary = new PaymentProcessSummary();
        summary.setPeriod(period);
        summary.setStartTime(LocalDateTime.now());

        CompanyResponse company = fetchCompany();
        log.info("Processing company: {} (ID: {})", company.getName(), company.getId());

        processCompany(company);

        summary.setEndTime(LocalDateTime.now());
        summary.setDurationMs(System.currentTimeMillis() - startTime);

        logFinalSummary();
    }

    private void validateInputs() {
        if (period == null) {
            throw new IllegalArgumentException("Period cannot be null");
        }
        if (companyId == null) {
            throw new IllegalArgumentException("CompanyId cannot be null");
        }
    }

    private CompanyResponse fetchCompany() {
        CompanyGetCmd cmd = commandFactory.createCommand(CompanyGetCmd.class);
        cmd.setCompanyId(companyId.toString());
        cmd.execute();

        return cmd.getCompanyResponse();
    }

    private void processCompany(CompanyResponse company) {
        CompanyProcessResult result = new CompanyProcessResult();
        result.setCompanyId(UUID.fromString(company.getId()));
        result.setCompanyName(company.getName());
        result.setStartTime(LocalDateTime.now());

        try {
            int totalEmployees = processAllEmployeesForCompany(UUID.fromString(company.getId()), result);
            result.setTotalEmployees(totalEmployees);
            result.setSuccess(true);

            log.info("Company {} processed successfully: {} employees, {} payments created, {} errors",
                    company.getName(), totalEmployees, result.getSuccessfulPayments(), result.getFailedPayments());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            log.error("Error processing company {}: {}", company.getName(), e.getMessage(), e);
            throw new RuntimeException("Failed to process company: " + company.getName(), e);
        }

        result.setEndTime(LocalDateTime.now());
        summary.addCompanyResult(result);
    }

    private int processAllEmployeesForCompany(UUID companyId, CompanyProcessResult result) {
        int pageNumber = 0;
        int totalEmployees = 0;
        boolean hasMore = true;

        processPendingItems(companyId, result);

        while (hasMore) {
            Pageable pageable = PageRequest.of(pageNumber, EMPLOYEE_PAGE_SIZE);
            Page<PayrollEmployeeResponse> page = fetchPayrollPage(companyId, pageable);

            if (page.isEmpty()) {
                hasMore = false;
                continue;
            }

            log.debug("Processing page {} for company {} - {} employees",
                    pageNumber, companyId, page.getNumberOfElements());

            processPayrollPage(page.getContent(), companyId, result);

            totalEmployees += page.getNumberOfElements();
            pageNumber++;
            hasMore = page.hasNext();
        }

        return totalEmployees;
    }

    private void processPendingItems(UUID companyId, CompanyProcessResult result) {
        log.info("Processing pending items for company {}", companyId);

        try {
            AdvanceProcessCmd advanceCmd = commandFactory.createCommand(AdvanceProcessCmd.class);
            advanceCmd.setCompanyId(companyId);
            advanceCmd.setPeriod(period);
            advanceCmd.execute();
            result.setProcessedAdvances(advanceCmd.getProcessedList().size());

            AbsenceProcessCmd absenceCmd = commandFactory.createCommand(AbsenceProcessCmd.class);
            absenceCmd.setCompanyId(companyId);
            absenceCmd.setPeriod(period);
            absenceCmd.execute();
            result.setProcessedAbsences(absenceCmd.getProcessedList().size());

            SalaryEventProcessCmd eventCmd = commandFactory.createCommand(SalaryEventProcessCmd.class);
            eventCmd.setCompanyId(companyId);
            eventCmd.setPeriod(period);
            eventCmd.execute();
            result.setProcessedEvents(eventCmd.getProcessedList().size());

            log.info("Pending items processed - Advances: {}, Absences: {}, Events: {}",
                    result.getProcessedAdvances(), result.getProcessedAbsences(), result.getProcessedEvents());
        } catch (Exception e) {
            log.error("Error processing pending items for company {}: {}", companyId, e.getMessage(), e);
        }
    }

    private Page<PayrollEmployeeResponse> fetchPayrollPage(UUID companyId, Pageable pageable) {
        PayrollCalculateByCompanyCmd cmd = commandFactory.createCommand(PayrollCalculateByCompanyCmd.class);
        cmd.setCompanyId(companyId);
        cmd.setPageable(pageable);
        cmd.setPeriod(period);
        cmd.execute();
        return cmd.getPayrollPageResponse();
    }

    @Transactional
    protected void processPayrollPage(List<PayrollEmployeeResponse> payrolls, UUID companyId, CompanyProcessResult result) {
        List<Payment> payments = new ArrayList<>();

        for (PayrollEmployeeResponse payrollEmployee : payrolls) {
            try {
                Payment payment = createPayment(payrollEmployee, companyId);
                payments.add(payment);
                result.incrementSuccessfulPayments();
            } catch (Exception e) {
                result.incrementFailedPayments();
                log.error("Error creating payment for employee {}: {}",
                        payrollEmployee.getEmployee().getId(), e.getMessage(), e);
            }
        }

        if (!payments.isEmpty()) {
            paymentRepository.saveAll(payments);
            log.debug("Saved batch of {} payments", payments.size());
        }
    }

    private Payment createPayment(PayrollEmployeeResponse payrollEmployee, UUID companyId) {
        Payment payment = new Payment();

        payment.setPeriod(period);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setGrossAmount(payrollEmployee.getPayroll().getTotalEarnings()); // Ahora es totalEarnings
        payment.setTotalDeductions(payrollEmployee.getPayroll().getTotalDeductions());
        payment.setNetAmount(payrollEmployee.getPayroll().getNetAmount());
        payment.setCompanyId(companyId);

        Employee employee = employeeRepository.getReferenceById(UUID.fromString(payrollEmployee.getEmployee().getId()));
        payment.setEmployeeDetails(buildEmployeeDetails(employee));
        payment.setEmployeeId(employee.getId());

        PaymentDetails details = mapToPaymentDetails(payrollEmployee.getPayroll());
        payment.setPaymentDetails(details);

        return payment;
    }

    private PaymentDetails mapToPaymentDetails(PayrollResponse payroll) {
        PaymentDetails details = new PaymentDetails();

        details.setBaseSalary(payroll.getBaseSalary());
        details.setWorkedDays(payroll.getWorkedDays());
        details.setWorkingDaysPerMonth(payroll.getWorkingDaysPerMonth());
        details.setBasicEarnings(payroll.getBasicEarnings());

        details.setSeniorityYears(payroll.getSeniorityYears());
        details.setSeniorityIncreasePercentage(payroll.getSeniorityIncreasePercentage());
        details.setSeniorityBonus(payroll.getSeniorityBonus());

        details.setOtherBonuses(payroll.getOtherBonuses());
        details.setTotalBonuses(payroll.getTotalBonuses());
        details.setTotalEarnings(payroll.getTotalEarnings());

        details.setDeductionAfpPercentage(payroll.getDeductionAfpPercentage());
        details.setDeductionAfp(payroll.getDeductionAfp());

        details.setTotalDeductions(payroll.getTotalDeductions());
        details.setNetAmount(payroll.getNetAmount());

        if (payroll.getDeductions() != null) {
            List<PaymentDeduction> deductions = payroll.getDeductions().stream()
                    .map(this::mapToPaymentDeduction)
                    .toList();
            details.setDeductions(deductions);
        }

        return details;
    }

    private PaymentDeduction mapToPaymentDeduction(PayrollDeductionResponse response) {
        PaymentDeduction deduction = new PaymentDeduction();
        deduction.setType(response.getType());
        deduction.setQty(response.getQty());
        deduction.setTotalDeduction(response.getTotalDeduction());
        return deduction;
    }

    private void logFinalSummary() {
        log.info("=== Payment Processing Completed ===");
        log.info("Company ID: {}", companyId);
        log.info("Period: {}", summary.getPeriod());
        log.info("Duration: {} ms ({} seconds)", summary.getDurationMs(), summary.getDurationMs() / 1000.0);
        log.info("Total employees: {}", summary.getTotalEmployees());
        log.info("Total successful payments: {}", summary.getTotalSuccessfulPayments());
        log.info("Total failed payments: {}", summary.getTotalFailedPayments());
        log.info("Success rate: {}%", summary.getSuccessRate());

        if (summary.getTotalFailedPayments() > 0) {
            log.warn("There were {} failed payments. Review the logs for details.",
                    summary.getTotalFailedPayments());
        }

        CompanyProcessResult result = summary.getCompanyResults().getFirst();
        if (!result.isSuccess()) {
            log.error("Company {} failed: {}", result.getCompanyName(), result.getErrorMessage());
        } else if (result.getFailedPayments() > 0) {
            log.warn("Company {} had {} failed payments out of {} total",
                    result.getCompanyName(), result.getFailedPayments(), result.getTotalEmployees());
        }
    }

    private EmployeeDetails buildEmployeeDetails(Employee employee){
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setId(employee.getId());
        employeeDetails.setFirstName(employee.getFirstName());
        employeeDetails.setLastName(employee.getLastName());
        employeeDetails.setCi(employee.getCi());
        employeeDetails.setEmail(employee.getEmail());
        employeeDetails.setPhone(employee.getPhone());
        employeeDetails.setAddress(employee.getAddress());
        employeeDetails.setBirthDate(employee.getBirthDate());
        employeeDetails.setHireDate(employee.getHireDate());
        employeeDetails.setStatus(employee.getStatus());
        employeeDetails.setType(employee.getType());
        employeeDetails.setIsDeleted(employee.getIsDeleted());
        employeeDetails.setDeletedAt(employee.getDeletedAt());
        employeeDetails.setIsDisassociated(employee.getIsDisassociated());
        employeeDetails.setDisassociationDate(employee.getDisassociationDate());
        employeeDetails.setDisassociationReason(employee.getDisassociationReason());
        Optional.ofNullable(employee.getBranch()).ifPresent(b -> {
            employeeDetails.setBranchId(b.getId());
            employeeDetails.setBranchName(b.getName());
        });

        Optional.ofNullable(employee.getPosition()).ifPresent(p -> {
            employeeDetails.setPositionId(p.getId());
            employeeDetails.setPositionName(p.getName());
        });
        employeeDetails.setCompanyId(employee.getCompanyId());
        return employeeDetails;
    }
}
