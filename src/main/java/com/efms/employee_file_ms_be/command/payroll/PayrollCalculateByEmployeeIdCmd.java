package com.efms.employee_file_ms_be.command.payroll;

import com.efms.employee_file_ms_be.api.response.GeneralSettingsResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollDeductionResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollResponse;
import com.efms.employee_file_ms_be.command.absence.AbsenceListByEmployeeIdCmd;
import com.efms.employee_file_ms_be.command.advance.AdvanceListByEmployeeIdCmd;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.employee.EmployeeReadByIdCmd;
import com.efms.employee_file_ms_be.command.general_settings.GeneralSettingsReadCmd;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventListByEmployeeIdCmd;
import com.efms.employee_file_ms_be.model.domain.*;
import com.efms.employee_file_ms_be.util.DateUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class PayrollCalculateByEmployeeIdCmd implements Command {

    @Setter
    private String employeeId;

    @Setter
    private LocalDate startDate;

    @Setter
    private LocalDate endDate;

    @Setter
    private Integer period;

    @Setter
    private Boolean useActualDate;

    @Getter
    private PayrollResponse payrollResponse;

    private final CommandFactory commandFactory;

    private static final BigDecimal SENIORITY_MULTIPLIER = BigDecimal.valueOf(3);

    @Override
    public void execute() {
        if (Boolean.TRUE.equals(useActualDate)) {
            LocalDate today = LocalDate.now();
            startDate = today.withDayOfMonth(1);
            endDate = today;
        } else if (period != null) {
            startDate = DateUtils.getStartDateFromPeriod(period);
            endDate = DateUtils.getEndDateFromPeriod(period);
        } else {
            startDate = DateUtils.getStartDateOrDefault(startDate);
            endDate = DateUtils.getEndDateOrDefault(endDate);
        }
        GeneralSettingsResponse generalSettings = findGeneralSettings();
        Employee employee = findEmployee();
        BaseSalary salary = employee.getBaseSalary();

        BigDecimal baseSalaryAmount = (salary != null && salary.getAmount() != null)
                ? salary.getAmount()
                : BigDecimal.ZERO;

        int seniority = getSeniority(employee);
        int workedDays = Math.min(calculateWorkedDays(employee), generalSettings.getWorkingDaysPerMonth());
        Integer workingDaysPerMonth = generalSettings.getWorkingDaysPerMonth();

        BigDecimal basicEarnings = calculateBasicEarnings(baseSalaryAmount, workedDays, workingDaysPerMonth);

        BigDecimal seniorityPercentage = getSeniorityPercentage(seniority);

        BigDecimal seniorityFactor = employee.getType() == EmployeeType.CONSULTANT
                ? BigDecimal.ZERO
                : BigDecimal.ONE;

        BigDecimal seniorityBonus = calculateSeniorityBonus(
                baseSalaryAmount,
                seniorityPercentage,
                employee,
                workingDaysPerMonth
        ).multiply(seniorityFactor);

        List<Absence> absences = findAbsenceByEmployeeId();
        Map<AbsenceType, List<Absence>> absencesByType = absences.stream()
                .collect(Collectors.groupingBy(Absence::getType));

        List<Advance> advances = findAdvancesByEmployeeId();

        List<SalaryEvent> manualSalaryEvents = findManualSalaryEventsByEmployeeId();

        List<PayrollDeductionResponse> deductions = new ArrayList<>();

        absencesByType.forEach((type, absenceList) -> {
            PayrollDeductionResponse response = new PayrollDeductionResponse();
            response.setType(type.name());
            response.setQty(absenceList.size());
            response.setTotalDeduction(
                    absenceList.stream()
                            .filter(absence -> absence.getSalaryEvent() != null)
                            .map(absence -> absence.getSalaryEvent().getAmount())
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
            deductions.add(response);
        });

        if (!advances.isEmpty()) {
            PayrollDeductionResponse advanceDeduction = new PayrollDeductionResponse();
            advanceDeduction.setType("ADVANCE");
            advanceDeduction.setQty(advances.size());
            advanceDeduction.setTotalDeduction(
                    advances.stream()
                            .filter(advance -> advance.getSalaryEvent() != null)
                            .map(advance -> advance.getSalaryEvent().getAmount())
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
            deductions.add(advanceDeduction);
        }

        BigDecimal otherBonuses = manualSalaryEvents.stream()
                .filter(se -> se.getType() == SalaryEventType.BONUS)
                .map(SalaryEvent::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalBonuses = seniorityBonus.add(otherBonuses);

        BigDecimal manualDeductions = manualSalaryEvents.stream()
                .filter(se -> se.getType() == SalaryEventType.DEDUCTION)
                .map(SalaryEvent::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!manualSalaryEvents.isEmpty() && manualDeductions.compareTo(BigDecimal.ZERO) > 0) {
            PayrollDeductionResponse manualDeductionResponse = new PayrollDeductionResponse();
            manualDeductionResponse.setType("OTHER");
            manualDeductionResponse.setQty((int) manualSalaryEvents.stream()
                    .filter(se -> se.getType() == SalaryEventType.DEDUCTION)
                    .count());
            manualDeductionResponse.setTotalDeduction(manualDeductions);
            deductions.add(manualDeductionResponse);
        }

        BigDecimal totalEarnings = basicEarnings.add(totalBonuses);

        BigDecimal afpContribution = employee.getType() == EmployeeType.CONSULTANT
                ? BigDecimal.ZERO
                : calculateAfpContribution(totalEarnings, generalSettings.getContributionAfpPercentage());

        BigDecimal totalDeductions = deductions.stream()
                .map(PayrollDeductionResponse::getTotalDeduction)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(afpContribution);

        BigDecimal netAmount = totalEarnings
                .subtract(totalDeductions)
                .setScale(1, RoundingMode.HALF_UP);

        payrollResponse = new PayrollResponse();
        payrollResponse.setBaseSalary(baseSalaryAmount);
        payrollResponse.setWorkedDays(workedDays);
        payrollResponse.setWorkingDaysPerMonth(workingDaysPerMonth);
        payrollResponse.setBasicEarnings(basicEarnings);
        payrollResponse.setSeniorityYears(seniority);
        payrollResponse.setSeniorityIncreasePercentage(seniorityPercentage);
        payrollResponse.setSeniorityBonus(seniorityBonus);
        payrollResponse.setOtherBonuses(otherBonuses);
        payrollResponse.setTotalBonuses(totalBonuses);
        payrollResponse.setTotalEarnings(totalEarnings);
        payrollResponse.setDeductionAfpPercentage(generalSettings.getContributionAfpPercentage());
        payrollResponse.setDeductionAfp(afpContribution);
        payrollResponse.setDeductions(deductions);
        payrollResponse.setTotalDeductions(totalDeductions);
        payrollResponse.setNetAmount(netAmount);
    }

    private int calculateWorkedDays(Employee employee) {
        LocalDate effectiveStartDate = startDate;
        LocalDate effectiveEndDate = endDate;

        if (employee.getHireDate().isAfter(effectiveStartDate)) {
            effectiveStartDate = employee.getHireDate();
        }

        if (employee.getStatus() == EmployeeStatus.DELETED && employee.getDeletedAt() != null) {
            LocalDate deletedDate = employee.getDeletedAt().toLocalDate();
            if (deletedDate.isBefore(effectiveEndDate)) {
                effectiveEndDate = deletedDate;
            }
        }

        if (employee.getDisassociationDate() != null) {
            LocalDate disassociationDate = employee.getDisassociationDate();
            if (disassociationDate.isBefore(effectiveEndDate)) {
                effectiveEndDate = disassociationDate;
            }
        }

        LocalDate today = LocalDate.now();
        if (effectiveEndDate.isAfter(today)) {
            effectiveEndDate = today;
        }

        if (effectiveStartDate.isAfter(effectiveEndDate)) {
            return 0;
        }

        return (int) ChronoUnit.DAYS.between(effectiveStartDate, effectiveEndDate) + 1;
    }

    private BigDecimal calculateBasicEarnings(BigDecimal baseSalary, int workedDays, Integer workingDaysPerMonth) {
        if (workingDaysPerMonth == null || workingDaysPerMonth == 0) {
            workingDaysPerMonth = 30;
        }
        return baseSalary
                .multiply(BigDecimal.valueOf(workedDays))
                .divide(BigDecimal.valueOf(workingDaysPerMonth), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getSeniorityPercentage(int seniority) {
        if (seniority >= 2 && seniority <= 4) {
            return new BigDecimal("5");
        } else if (seniority >= 5 && seniority <= 7) {
            return new BigDecimal("11");
        } else if (seniority >= 8 && seniority <= 10) {
            return new BigDecimal("18");
        } else if (seniority > 10) {
            return new BigDecimal("18");
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateSeniorityBonus(BigDecimal baseSalary, BigDecimal percentage, Employee employee, Integer workingDaysPerMonth) {
        if (percentage.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal fullBonus = baseSalary
                .multiply(percentage)
                .multiply(SENIORITY_MULTIPLIER)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        int proratedDays = calculateSeniorityProratedDays(employee, workingDaysPerMonth);

        if (proratedDays >= workingDaysPerMonth) {
            return fullBonus;
        }

        if (proratedDays <= 0) {
            return BigDecimal.ZERO;
        }

        return fullBonus
                .multiply(BigDecimal.valueOf(proratedDays))
                .divide(BigDecimal.valueOf(workingDaysPerMonth), 2, RoundingMode.HALF_UP);
    }

    private int calculateSeniorityProratedDays(Employee employee, Integer workingDaysPerMonth) {
        LocalDate hireDate = employee.getHireDate();

        LocalDate seniorityDate;
        try {
            seniorityDate = startDate.withDayOfMonth(hireDate.getDayOfMonth());
        } catch (java.time.DateTimeException e) {
            seniorityDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        }

        // If the employee has a disassociation date, use it as the effective end date
        LocalDate effectiveEndDate = endDate;
        if (employee.getDisassociationDate() != null && employee.getDisassociationDate().isBefore(effectiveEndDate)) {
            effectiveEndDate = employee.getDisassociationDate();
        }

        if (!seniorityDate.isAfter(startDate)) {
            // Already earned seniority before the period, but still prorated by disassociation
            return (int) ChronoUnit.DAYS.between(startDate, effectiveEndDate);
        }

        if (seniorityDate.isAfter(effectiveEndDate)) {
            return 0;
        }

        return (int) ChronoUnit.DAYS.between(seniorityDate, effectiveEndDate);
    }

    private BigDecimal calculateAfpContribution(BigDecimal totalEarnings, BigDecimal afpContributionPercentage) {
        if (totalEarnings == null || afpContributionPercentage == null) {
            return BigDecimal.ZERO;
        }
        return totalEarnings.multiply(afpContributionPercentage);
    }

    private GeneralSettingsResponse findGeneralSettings() {
        GeneralSettingsReadCmd command = commandFactory.createCommand(GeneralSettingsReadCmd.class);
        command.execute();
        return command.getGeneralSettingsResponse();
    }

    private Employee findEmployee() {
        EmployeeReadByIdCmd command = commandFactory.createCommand(EmployeeReadByIdCmd.class);
        command.setId(employeeId);
        command.execute();
        return command.getEmployee();
    }

    private int getSeniority(Employee employee) {
        LocalDate hireDate = employee.getHireDate();
        return Period.between(hireDate, LocalDate.now()).getYears();
    }

    private List<Absence> findAbsenceByEmployeeId() {
        AbsenceListByEmployeeIdCmd command = commandFactory.createCommand(AbsenceListByEmployeeIdCmd.class);
        command.setEmployeeId(employeeId);
        command.setStartDate(startDate);
        command.setEndDate(endDate);
        command.execute();
        return command.getAbsenceList();
    }

    private List<Advance> findAdvancesByEmployeeId() {
        AdvanceListByEmployeeIdCmd command = commandFactory.createCommand(AdvanceListByEmployeeIdCmd.class);
        command.setEmployeeId(employeeId);
        command.setStartDate(startDate);
        command.setEndDate(endDate);
        command.execute();
        return command.getAdvanceList();
    }

    private List<SalaryEvent> findManualSalaryEventsByEmployeeId() {
        SalaryEventListByEmployeeIdCmd command = commandFactory.createCommand(SalaryEventListByEmployeeIdCmd.class);
        command.setEmployeeId(employeeId);
        command.setCategory(SalaryEventCategory.MANUAL);
        command.setStartDate(startDate);
        command.setEndDate(endDate);
        command.execute();
        return command.getSalaryEventList();
    }
}
