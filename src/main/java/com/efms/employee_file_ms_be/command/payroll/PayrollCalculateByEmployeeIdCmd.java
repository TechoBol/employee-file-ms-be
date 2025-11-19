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

    @Getter
    private PayrollResponse payrollResponse;

    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        if (period != null) {
            startDate = DateUtils.getStartDateFromPeriod(period);
            endDate = DateUtils.getEndDateFromPeriod(period);
        } else {
            startDate = DateUtils.getStartDateOrDefault(startDate);
            endDate = DateUtils.getEndDateOrDefault(endDate);
        }
        GeneralSettingsResponse generalSettings = findGeneralSettings();
        Employee employee = findEmployee();
        BaseSalary salary = employee.getBaseSalary();
        int seniority = getSeniority(employee);

        int workedDays = calculateWorkedDays(employee);
        Integer workingDaysPerMonth = generalSettings.getWorkingDaysPerMonth();

        // Sueldo básico = (haber básico * días trabajados) / días del mes
        BigDecimal basicEarnings = calculateBasicEarnings(salary.getAmount(), workedDays, workingDaysPerMonth);

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

        // Otros bonos (bonos manuales)
        BigDecimal otherBonuses = manualSalaryEvents.stream()
                .filter(se -> se.getType() == SalaryEventType.BONUS)
                .map(SalaryEvent::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Total de bonos (antigüedad + otros)
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

        BigDecimal totalDeductions = deductions.stream()
                .map(PayrollDeductionResponse::getTotalDeduction)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Total ganado = sueldo básico + total de bonos
        BigDecimal totalEarnings = basicEarnings.add(totalBonuses);

        // Líquido pagable = total ganado - AFP - total descuentos
        BigDecimal netAmount = totalEarnings
                .subtract(afpContribution)
                .subtract(totalDeductions);

        payrollResponse = new PayrollResponse();
        payrollResponse.setBaseSalary(salary.getAmount()); // Haber básico
        payrollResponse.setWorkedDays(workedDays);
        payrollResponse.setWorkingDaysPerMonth(workingDaysPerMonth);
        payrollResponse.setBasicEarnings(basicEarnings); // Sueldo básico calculado
        payrollResponse.setSeniorityYears(seniority);
        payrollResponse.setSeniorityIncreasePercentage(generalSettings.getSeniorityIncreasePercentage());
        payrollResponse.setSeniorityBonus(seniorityBonus);
        payrollResponse.setOtherBonuses(otherBonuses); // Otros bonos (manuales)
        payrollResponse.setTotalBonuses(totalBonuses); // Total de bonos
        payrollResponse.setTotalEarnings(totalEarnings); // Total ganado
        payrollResponse.setDeductionAfpPercentage(generalSettings.getContributionAfpPercentage());
        payrollResponse.setDeductionAfp(afpContribution);
        payrollResponse.setDeductions(deductions);
        payrollResponse.setTotalDeductions(totalDeductions);
        payrollResponse.setNetAmount(netAmount); // Líquido pagable
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

        if (effectiveStartDate.isAfter(effectiveEndDate)) {
            return 0;
        }

        return (int) ChronoUnit.DAYS.between(effectiveStartDate, effectiveEndDate) + 1;
    }

    private BigDecimal calculateBasicEarnings(BigDecimal baseSalary, int workedDays, Integer workingDaysPerMonth) {
        if (workingDaysPerMonth == null || workingDaysPerMonth == 0) {
            workingDaysPerMonth = 30; // Default
        }
        return baseSalary
                .multiply(BigDecimal.valueOf(workedDays))
                .divide(BigDecimal.valueOf(workingDaysPerMonth), 2, RoundingMode.HALF_UP);
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
