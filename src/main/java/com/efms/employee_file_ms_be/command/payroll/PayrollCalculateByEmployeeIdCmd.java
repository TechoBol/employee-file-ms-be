package com.efms.employee_file_ms_be.command.payroll;

import com.efms.employee_file_ms_be.api.response.GeneralSettingsResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollDeductionResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollResponse;
import com.efms.employee_file_ms_be.command.absence.AbsenceListByEmployeeIdCmd;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.employee.EmployeeReadByIdCmd;
import com.efms.employee_file_ms_be.command.general_settings.GeneralSettingsReadCmd;
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
        List<Absence> permissions = absencesByType.getOrDefault(AbsenceType.PERMISSION, Collections.emptyList());
        List<Absence> absencesOnly = absencesByType.getOrDefault(AbsenceType.ABSENCE, Collections.emptyList());
        List<Absence> vacations = absencesByType.getOrDefault(AbsenceType.VACATION, Collections.emptyList());

        List<PayrollDeductionResponse> deductions = absencesByType.entrySet().stream()
                .map(entry -> {
                    PayrollDeductionResponse response = new PayrollDeductionResponse();
                    response.setType(entry.getKey().name());
                    response.setQty(entry.getValue().size());
                    response.setTotalDeduction(
                            entry.getValue().stream()
                                    .filter(absence -> absence.getSalaryEvent() != null)
                                    .map(absence -> absence.getSalaryEvent().getAmount())
                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    );
                    return response;
                })
                .toList();

        BigDecimal totalDeductions = deductions.stream()
                .map(PayrollDeductionResponse::getTotalDeduction)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = basicEarnings
                .add(seniorityBonus)
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

        // Determinar el mes objetivo según la regla de los 5 días
        if (now.getDayOfMonth() <= 5) {
            targetMonth = now.minusMonths(1);
        } else {
            targetMonth = now;
        }

        LocalDate startOfTargetMonth = targetMonth.withDayOfMonth(1);
        LocalDate endOfTargetMonth = targetMonth.withDayOfMonth(targetMonth.lengthOfMonth());

        // Fecha de inicio efectiva (la más tardía entre inicio del mes y fecha de contratación)
        LocalDate effectiveStartDate = employee.getHireDate().isBefore(startOfTargetMonth)
                ? startOfTargetMonth
                : employee.getHireDate();

        // Fecha de fin efectiva
        LocalDate effectiveEndDate = endOfTargetMonth;

        // Si el empleado está eliminado, ajustar la fecha de fin
        if (employee.getStatus() == EmployeeStatus.DELETED && employee.getDeletedAt() != null) {
            LocalDate deletedDate = employee.getDeletedAt().toLocalDate();
            if (!deletedDate.isBefore(startOfTargetMonth) && !deletedDate.isAfter(endOfTargetMonth)) {
                effectiveEndDate = deletedDate;
            }
        }

        // Si el mes objetivo es el actual y aún no ha terminado
        if (targetMonth.getYear() == now.getYear() && targetMonth.getMonth() == now.getMonth()) {
            effectiveEndDate = now;
        }

        // Verificar que el empleado estuvo activo en el mes objetivo
        if (effectiveStartDate.isAfter(endOfTargetMonth) || effectiveEndDate.isBefore(startOfTargetMonth)) {
            return 0;
        }

        // Asegurar que las fechas estén dentro del rango del mes
        if (effectiveStartDate.isBefore(startOfTargetMonth)) {
            effectiveStartDate = startOfTargetMonth;
        }
        if (effectiveEndDate.isAfter(endOfTargetMonth)) {
            effectiveEndDate = endOfTargetMonth;
        }

        // Calcular días trabajados (inclusivo)
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

    private BigDecimal sumSalaryEvents(List<Absence> absences) {
        return absences.stream()
                .map(Absence::getSalaryEvent)
                .filter(Objects::nonNull)
                .map(SalaryEvent::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
}
