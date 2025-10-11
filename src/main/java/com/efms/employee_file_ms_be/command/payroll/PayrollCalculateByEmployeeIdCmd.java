package com.efms.employee_file_ms_be.command.payroll;

import com.efms.employee_file_ms_be.api.response.GeneralSettingsResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollDeductionResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollResponse;
import com.efms.employee_file_ms_be.command.absence.AbsenceListByEmployeeIdCmd;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.employee.EmployeeReadByIdCmd;
import com.efms.employee_file_ms_be.command.general_settings.GeneralSettingsReadByCompanyIdCmd;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
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
        LocalDate startOfMonth = now.withDayOfMonth(1);
        int currentDay = now.getDayOfMonth();

        // Si el empleado está eliminado y fue eliminado este mes
        if (employee.getStatus() == EmployeeStatus.DELETED && employee.getDeletedAt() != null) {
            LocalDate deletedDate = employee.getDeletedAt().toLocalDate();
            if (deletedDate.getYear() == now.getYear() && deletedDate.getMonth() == now.getMonth()) {
                return deletedDate.getDayOfMonth();
            }
        }

        // Si estamos en los primeros 5 días del mes
        if (currentDay <= 5) {
            // Verificar si fue contratado este mes
            LocalDate hireDate = employee.getHireDate();
            if (hireDate.getYear() == now.getYear() && hireDate.getMonth() == now.getMonth()) {
                return currentDay;
            }
            // Mes completo anterior
            return 30;
        }

        return currentDay;
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
        GeneralSettingsReadByCompanyIdCmd command = commandFactory.createCommand(GeneralSettingsReadByCompanyIdCmd.class);
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
