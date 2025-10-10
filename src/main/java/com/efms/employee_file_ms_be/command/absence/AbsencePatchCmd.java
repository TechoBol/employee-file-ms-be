package com.efms.employee_file_ms_be.command.absence;

import com.efms.employee_file_ms_be.api.request.AbsenceUpdateRequest;
import com.efms.employee_file_ms_be.api.response.AbsenceResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventByAbsenceCreateCmd;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventDeleteByIdCmd;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.AbsenceNotFoundException;
import com.efms.employee_file_ms_be.exception.EndDateBeforeStartDateException;
import com.efms.employee_file_ms_be.exception.RecordEditNotAllowedException;
import com.efms.employee_file_ms_be.model.domain.Absence;
import com.efms.employee_file_ms_be.model.domain.AbsenceDuration;
import com.efms.employee_file_ms_be.model.domain.AbsenceType;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.mapper.absence.AbsenceMapper;
import com.efms.employee_file_ms_be.model.repository.AbsenceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class AbsencePatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private AbsenceUpdateRequest absenceUpdateRequest;

    @Getter
    private AbsenceResponse absenceResponse;

    private final AbsenceRepository absenceRepository;
    private final AbsenceMapper absenceMapper;
    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Absence absence = absenceRepository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new AbsenceNotFoundException(id));

        validateEditingTimeframe(absence);
        validateRequest(absence);

        AbsenceType previousType = absence.getType();

        updateProperties(absence, absenceUpdateRequest);

        handleSalaryEventUpdate(absence, previousType);

        absence = absenceRepository.save(absence);
        absenceResponse = absenceMapper.toDTO(absence);
    }

    private void validateEditingTimeframe(Absence absence) {
        LocalDate now = LocalDate.now();
        LocalDate absenceDate = absence.getDate();

        LocalDate lastDayOfAbsenceMonth = absenceDate.withDayOfMonth(absenceDate.lengthOfMonth());

        LocalDate fifthDayOfNextMonth = lastDayOfAbsenceMonth.plusDays(5);

        if (now.isAfter(fifthDayOfNextMonth)) {
            throw new RecordEditNotAllowedException();
        }
    }

    private void validateRequest(Absence absence) {
        LocalDate startDate = absenceUpdateRequest.getDate() != null ?
                absenceUpdateRequest.getDate() : absence.getDate();

        LocalDate endDate = absenceUpdateRequest.getEndDate() != null ?
                absenceUpdateRequest.getEndDate() : absence.getEndDate();

        if (endDate != null && endDate.isBefore(startDate)) {
            throw new EndDateBeforeStartDateException(startDate.atStartOfDay(), endDate.atStartOfDay());
        }

        AbsenceType type = absenceUpdateRequest.getType() != null ?
                absenceUpdateRequest.getType() : absence.getType();

        AbsenceDuration duration = absenceUpdateRequest.getDuration() != null ?
                absenceUpdateRequest.getDuration() : absence.getDuration();

        if (type == AbsenceType.VACATION &&
                endDate != null &&
                duration == AbsenceDuration.HALF_DAY) {
            throw new EndDateBeforeStartDateException();
        }
    }

    private void updateProperties(Absence absence, AbsenceUpdateRequest request) {
        Optional.ofNullable(request.getType()).ifPresent(absence::setType);
        Optional.ofNullable(request.getDuration()).ifPresent(absence::setDuration);
        Optional.ofNullable(request.getDate()).ifPresent(absence::setDate);
        Optional.ofNullable(request.getEndDate()).ifPresent(absence::setEndDate);
        Optional.ofNullable(request.getReason()).ifPresent(absence::setReason);
        Optional.ofNullable(request.getDescription()).ifPresent(absence::setDescription);
    }

    private void handleSalaryEventUpdate(Absence absence, AbsenceType previousType) {
        boolean hadSalaryEvent = absence.getSalaryEvent() != null;
        boolean shouldHaveSalaryEvent = absence.getType() != AbsenceType.VACATION;

        if (hadSalaryEvent && !shouldHaveSalaryEvent) {
            deleteSalaryEvent(absence.getSalaryEvent().getId());
            absence.setSalaryEvent(null);

        } else if (!hadSalaryEvent && shouldHaveSalaryEvent) {
            SalaryEvent salaryEvent = createSalaryEventForAbsence(absence);
            absence.setSalaryEvent(salaryEvent);

        } else if (hadSalaryEvent && shouldHaveSalaryEvent) {
            if (hasRelevantChanges(absence, previousType)) {
                deleteSalaryEvent(absence.getSalaryEvent().getId());
                SalaryEvent newSalaryEvent = createSalaryEventForAbsence(absence);
                absence.setSalaryEvent(newSalaryEvent);
            }
        }
    }

    private boolean hasRelevantChanges(Absence absence, AbsenceType previousType) {
        return absenceUpdateRequest.getType() != null ||
                absenceUpdateRequest.getDuration() != null ||
                absenceUpdateRequest.getDate() != null ||
                absenceUpdateRequest.getEndDate() != null;
    }

    private void deleteSalaryEvent(UUID salaryEventId) {
        SalaryEventDeleteByIdCmd command = commandFactory.createCommand(SalaryEventDeleteByIdCmd.class);
        command.setId(salaryEventId);
        command.execute();
    }

    private SalaryEvent createSalaryEventForAbsence(Absence absence) {
        SalaryEventByAbsenceCreateCmd command = commandFactory.createCommand(SalaryEventByAbsenceCreateCmd.class);
        command.setAbsence(absence);
        command.execute();
        return command.getSalaryEvent();
    }
}