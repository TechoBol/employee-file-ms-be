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

    @Getter
    private PayrollResponse payrollResponse;

    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        GeneralSettingsResponse generalSettings = findGeneralSettings();
        Employee employee = findEmployee();
        BaseSalary salary = employee.getBaseSalary();
        int seniority = getSeniority(employee);

        int workedDays = calculateWorkedDays(employee);
        BigDecimal basicEarnings = calculateBasicEarnings(salary.getAmount(), workedDays);

        BigDecimal seniorityBonus = calculateSeniorityBonus(salary.getAmount(), seniority, generalSettings.getSeniorityIncreasePercentage());
        BigDecimal afpContribution = calculateAfpContribution(salary.getAmount(), generalSettings.getContributionAfpPercentage());

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

        BigDecimal manualBonuses = manualSalaryEvents.stream()
                .filter(se -> se.getType() == SalaryEventType.BONUS)
                .map(SalaryEvent::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

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

        BigDecimal totalDeductions = deductions.stream()
                .map(PayrollDeductionResponse::getTotalDeduction)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = basicEarnings
                .add(seniorityBonus)
                .add(manualBonuses)
                .subtract(afpContribution)
                .subtract(totalDeductions);

        payrollResponse = new PayrollResponse();
        payrollResponse.setBaseSalary(salary.getAmount());
        payrollResponse.setWorkedDays(workedDays);
        payrollResponse.setBasicEarnings(basicEarnings);
        payrollResponse.setSeniorityYears(seniority);
        payrollResponse.setSeniorityIncreasePercentage(generalSettings.getSeniorityIncreasePercentage());
        payrollResponse.setSeniorityBonus(seniorityBonus);
        payrollResponse.setDeductionAfpPercentage(generalSettings.getContributionAfpPercentage());
        payrollResponse.setDeductionAfp(afpContribution);
        payrollResponse.setDeductions(deductions);
        payrollResponse.setTotalDeductions(totalDeductions);
        payrollResponse.setTotalAmount(totalAmount);
    }

    private int calculateWorkedDays(Employee employee) {
        LocalDate now = LocalDate.now();
        LocalDate targetMonth;

        if (now.getDayOfMonth() <= 5) {
            targetMonth = now.minusMonths(1);
        } else {
            targetMonth = now;
        }

        LocalDate startOfTargetMonth = targetMonth.withDayOfMonth(1);
        LocalDate endOfTargetMonth = targetMonth.withDayOfMonth(targetMonth.lengthOfMonth());

        LocalDate effectiveStartDate = employee.getHireDate().isBefore(startOfTargetMonth)
                ? startOfTargetMonth
                : employee.getHireDate();

        LocalDate effectiveEndDate = endOfTargetMonth;

        if (employee.getStatus() == EmployeeStatus.DELETED && employee.getDeletedAt() != null) {
            LocalDate deletedDate = employee.getDeletedAt().toLocalDate();
            if (!deletedDate.isBefore(startOfTargetMonth) && !deletedDate.isAfter(endOfTargetMonth)) {
                effectiveEndDate = deletedDate;
            }
        }

        if (targetMonth.getYear() == now.getYear() && targetMonth.getMonth() == now.getMonth()) {
            effectiveEndDate = now;
        }

        if (effectiveStartDate.isAfter(endOfTargetMonth) || effectiveEndDate.isBefore(startOfTargetMonth)) {
            return 0;
        }

        if (effectiveStartDate.isBefore(startOfTargetMonth)) {
            effectiveStartDate = startOfTargetMonth;
        }
        if (effectiveEndDate.isAfter(endOfTargetMonth)) {
            effectiveEndDate = endOfTargetMonth;
        }

        return (int) ChronoUnit.DAYS.between(effectiveStartDate, effectiveEndDate) + 1;
    }

    private BigDecimal calculateBasicEarnings(BigDecimal baseSalary, int workedDays) {
        return baseSalary
                .multiply(BigDecimal.valueOf(workedDays))
                .divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateSeniorityBonus(BigDecimal baseSalaryAmount, int seniority, BigDecimal seniorityIncreasePercentage) {
        if (seniority <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal bonusRate = seniorityIncreasePercentage
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(seniority));

        return baseSalaryAmount
                .multiply(bonusRate)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateAfpContribution(BigDecimal baseSalary, BigDecimal afpContributionPercentage) {
        if (baseSalary == null || afpContributionPercentage == null) {
            return BigDecimal.ZERO;
        }
        return baseSalary.multiply(afpContributionPercentage);
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
        command.execute();
        return command.getAbsenceList();
    }

    private List<Advance> findAdvancesByEmployeeId() {
        AdvanceListByEmployeeIdCmd command = commandFactory.createCommand(AdvanceListByEmployeeIdCmd.class);
        command.setEmployeeId(employeeId);
        command.execute();
        return command.getAdvanceList();
    }

    private List<SalaryEvent> findManualSalaryEventsByEmployeeId() {
        SalaryEventListByEmployeeIdCmd command = commandFactory.createCommand(SalaryEventListByEmployeeIdCmd.class);
        command.setEmployeeId(employeeId);
        command.setCategory(SalaryEventCategory.MANUAL.name());
        command.execute();
        return command.getSalaryEventList();
    }
}
