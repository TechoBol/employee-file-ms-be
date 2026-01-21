package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.request.SalaryEventCreateRequest;
import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.base_salary.BaseSalaryReadByEmployeeIdCmd;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.general_settings.GeneralSettingsReadCmd;
import com.efms.employee_file_ms_be.exception.CommonBadRequestException;
import com.efms.employee_file_ms_be.exception.Constants;
import com.efms.employee_file_ms_be.model.domain.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
@Validated
public class SalaryEventByAbsenceCreateCmd implements Command {

    @NotNull
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

        GeneralSettings generalSettings = findGeneralSettings();

        Integer workingDaysPerMonth = generalSettings.getWorkingDaysPerMonth();

        BigDecimal baseSalary = findBaseSalaryByEmployeeId(absence.getEmployee().getId().toString()).getAmount();

        BigDecimal deductionAmount = calculateDeductionAmount(
                baseSalary,
                absence.getType(),
                absence.getDuration(),
                getTotalDays(absence),
                workingDaysPerMonth
        );

        SalaryEventCreateRequest salaryEventCreateRequest = buildSalaryEventCreateRequest(
                absence,
                deductionAmount
        );

        salaryEvent = createSalaryEvent(salaryEventCreateRequest);
    }

    private void validateAbsence() {
        if (absence.getEmployee() == null || absence.getEmployee().getId() == null) {
            throw new CommonBadRequestException(Constants.ExceptionMessage.ABSENCE_EMPLOYEE_NOT_NULL);
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
        request.setCategory(SalaryEventCategory.ABSENCE);
        request.setDescription(buildDeductionDescription(absence));
        request.setAmount(deductionAmount);
        request.setFrequency(SalaryEventFrequency.ONE_TIME);
        request.setStartDate(absence.getDate());
        return request;
    }

    private BigDecimal calculateDeductionAmount(BigDecimal baseSalary, AbsenceType type,
                                                AbsenceDuration duration, int totalDays, int workingDaysPerMonth) {
        BigDecimal dailySalary = baseSalary.divide(BigDecimal.valueOf(workingDaysPerMonth), 4, RoundingMode.HALF_UP);

        BigDecimal durationFactor = (type == AbsenceType.PERMISSION && duration == AbsenceDuration.HALF_DAY) ?
                new BigDecimal("0.5") : BigDecimal.ONE;

        BigDecimal typeFactor = calculateTypeFactor(type, duration);

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

    private GeneralSettings findGeneralSettings() {
        GeneralSettingsReadCmd command = commandFactory.createCommand(GeneralSettingsReadCmd.class);
        command.execute();
        return command.getGeneralSettings();
    }
}
