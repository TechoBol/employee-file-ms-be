package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.request.SalaryEventCreateRequest;
import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.base_salary.BaseSalaryReadByEmployeeIdCmd;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.model.domain.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class SalaryEventByAbsenceCreateCmd implements Command {

    @Setter
    private Absence absence;

    @Getter
    private SalaryEvent salaryEvent;

    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        validateAbsence();

        if (absence.getType() == AbsenceType.VACATION) {
            salaryEvent = null;
            return;
        }

        BigDecimal baseSalary = findBaseSalaryByEmployeeId(absence.getEmployee().getId().toString()).getAmount();

        BigDecimal deductionAmount = calculateDeductionAmount(
                baseSalary,
                absence.getType(),
                absence.getDuration(),
                getTotalDays(absence)
        );

        SalaryEventCreateRequest salaryEventCreateRequest = buildSalaryEventCreateRequest(
                absence,
                deductionAmount
        );

        salaryEvent = createSalaryEvent(salaryEventCreateRequest);
    }

    private void validateAbsence() {
        if (absence == null) {
            throw new IllegalArgumentException("La ausencia no puede ser nula");
        }
        if (absence.getEmployee() == null || absence.getEmployee().getId() == null) {
            throw new IllegalArgumentException("El empleado de la ausencia no puede ser nulo");
        }
    }

    private BaseSalaryResponse findBaseSalaryByEmployeeId(String employeeId) {
        BaseSalaryReadByEmployeeIdCmd command = commandFactory.createCommand(BaseSalaryReadByEmployeeIdCmd.class);
        command.setEmployeeId(employeeId);
        command.execute();
        return command.getBaseSalaryResponse();
    }

    private SalaryEvent createSalaryEvent(SalaryEventCreateRequest request) {
        SalaryEventCreateCmd command = commandFactory.createCommand(SalaryEventCreateCmd.class);
        command.setSalaryEventCreateRequest(request);
        command.execute();
        return command.getSalaryEvent();
    }

    private SalaryEventCreateRequest buildSalaryEventCreateRequest(Absence absence, BigDecimal deductionAmount) {
        SalaryEventCreateRequest request = new SalaryEventCreateRequest();
        request.setEmployeeId(absence.getEmployee().getId().toString());
        request.setType(SalaryEventType.DEDUCTION);
        request.setDescription(buildDeductionDescription(absence));
        request.setAmount(deductionAmount);
        request.setFrequency(SalaryEventFrequency.ONE_TIME);
        request.setStartDate(absence.getDate());
        return request;
    }

    private BigDecimal calculateDeductionAmount(BigDecimal baseSalary, AbsenceType type,
                                                AbsenceDuration duration, int totalDays) {

        // Salario diario = salario base / 30
        BigDecimal dailySalary = baseSalary.divide(BigDecimal.valueOf(30), 4, RoundingMode.HALF_UP);

        // Factor según duración
        BigDecimal durationFactor = duration == AbsenceDuration.HALF_DAY ?
                new BigDecimal("0.5") : BigDecimal.ONE;

        // Factor según tipo de ausencia
        BigDecimal typeFactor = calculateTypeFactor(type, duration);

        // Cálculo final
        return dailySalary
                .multiply(typeFactor)
                .multiply(durationFactor)
                .multiply(BigDecimal.valueOf(totalDays))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTypeFactor(AbsenceType type, AbsenceDuration duration) {
        switch (type) {
            case PERMISSION:
                return BigDecimal.ONE;
            case ABSENCE:
                // Falta: factor 2 para día completo, 1 para medio día
                return duration == AbsenceDuration.HALF_DAY ?
                        BigDecimal.ONE : BigDecimal.valueOf(2);
            default:
                return BigDecimal.ONE;
        }
    }

    private int getTotalDays(Absence absence) {
        if (absence.getEndDate() == null) {
            return 1;
        }
        return (int) ChronoUnit.DAYS.between(absence.getDate(), absence.getEndDate().plusDays(1));
    }

    private String buildDeductionDescription(Absence absence) {
        StringBuilder description = new StringBuilder();

        switch (absence.getType()) {
            case PERMISSION:
                description.append("Descuento por permiso");
                break;
            case ABSENCE:
                description.append("Descuento por falta");
                break;
        }

        description.append(" - ");
        description.append(absence.getDuration() == AbsenceDuration.HALF_DAY ? "Medio día" : "Día completo");
        description.append(" - ");
        description.append(absence.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        if (absence.getEndDate() != null && !absence.getEndDate().equals(absence.getDate())) {
            description.append(" al ");
            description.append(absence.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        return description.toString();
    }
}
