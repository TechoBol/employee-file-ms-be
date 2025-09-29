package com.efms.employee_file_ms_be.command.absence;

import com.efms.employee_file_ms_be.api.request.AbsenceCreateRequest;
import com.efms.employee_file_ms_be.api.response.AbsenceResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventByAbsenceCreateCmd;
import com.efms.employee_file_ms_be.model.domain.*;
import com.efms.employee_file_ms_be.model.mapper.absence.AbsenceMapper;
import com.efms.employee_file_ms_be.model.repository.AbsenceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class AbsenceCreateCmd implements Command {

    @Setter
    private AbsenceCreateRequest absenceCreateRequest;

    @Getter
    private AbsenceResponse absenceResponse;

    private final AbsenceRepository absenceRepository;
    private final AbsenceMapper mapper;
    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        validateRequest();
        Absence absence = mapper.toEntity(absenceCreateRequest);

        // Crear el salary event si es necesario usando el nuevo command
        SalaryEvent salaryEvent = createSalaryEventForAbsence(absence);

        if (salaryEvent != null) {
            absence.setSalaryEvent(salaryEvent);
        }

        absence = absenceRepository.save(absence);
        absenceResponse = mapper.toDTO(absence);
    }

    private SalaryEvent createSalaryEventForAbsence(Absence absence) {
        SalaryEventByAbsenceCreateCmd command = commandFactory.createCommand(SalaryEventByAbsenceCreateCmd.class);
        command.setAbsence(absence);
        command.execute();
        return command.getSalaryEvent();
    }

    private void validateRequest() {
        if (absenceCreateRequest.getEndDate() != null &&
                absenceCreateRequest.getEndDate().isBefore(absenceCreateRequest.getDate())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        if (absenceCreateRequest.getType() == AbsenceType.VACATION &&
                absenceCreateRequest.getEndDate() != null &&
                absenceCreateRequest.getDuration() == AbsenceDuration.HALF_DAY) {
            throw new IllegalArgumentException("Las vacaciones de múltiples días deben ser de día completo");
        }
    }
}
