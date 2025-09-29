package com.efms.employee_file_ms_be.command.absence;

import com.efms.employee_file_ms_be.api.response.AbsenceResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Absence;
import com.efms.employee_file_ms_be.model.mapper.absence.AbsenceMapper;
import com.efms.employee_file_ms_be.model.repository.AbsenceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */

@CommandExecute
@RequiredArgsConstructor
public class AbsenceListByEmployeeIdCmd implements Command {

    @Setter
    private String employeeId;

    @Getter
    private List<AbsenceResponse> absenceResponseList;

    @Getter
    private List<Absence> absenceList;

    private final AbsenceRepository absenceRepository;
    private final AbsenceMapper absenceMapper;

    @Override
    public void execute() {
        absenceList = absenceRepository.findByEmployeeId(UUID.fromString(employeeId));
        absenceResponseList = absenceList.stream()
                .map(absenceMapper::toDTO)
                .toList();
    }
}
