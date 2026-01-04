package com.efms.employee_file_ms_be.command.absence;

import com.efms.employee_file_ms_be.api.request.AbsenceCreateRequest;
import com.efms.employee_file_ms_be.api.request.AbsenceUpdateRequest;
import com.efms.employee_file_ms_be.api.response.AbsenceResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.AbsenceNotFoundException;
import com.efms.employee_file_ms_be.model.domain.Absence;
import com.efms.employee_file_ms_be.model.repository.AbsenceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class AbsenceReplacePatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private AbsenceUpdateRequest absenceUpdateRequest;

    @Getter
    private AbsenceResponse absenceResponse;

    private final AbsenceRepository absenceRepository;
    private final CommandFactory commandFactory;

    @Transactional
    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Absence existingAbsence = absenceRepository.findByIdAndCompanyId(UUID.fromString(id), companyId, null)
                .orElseThrow(() -> new AbsenceNotFoundException(id));

        AbsenceCreateRequest createRequest = buildCreateRequest(existingAbsence);

        AbsenceDeleteCmd deleteCmd = commandFactory.createCommand(AbsenceDeleteCmd.class);
        deleteCmd.setId(id);
        deleteCmd.execute();

        AbsenceCreateCmd createCmd = commandFactory.createCommand(AbsenceCreateCmd.class);
        createCmd.setAbsenceCreateRequest(createRequest);
        createCmd.execute();

        absenceResponse = createCmd.getAbsenceResponse();
    }

    private AbsenceCreateRequest buildCreateRequest(Absence existing) {
        AbsenceCreateRequest request = new AbsenceCreateRequest();

        request.setEmployeeId(String.valueOf(existing.getEmployee().getId()));
        request.setType(absenceUpdateRequest.getType() != null ?
                absenceUpdateRequest.getType() : existing.getType());
        request.setDuration(absenceUpdateRequest.getDuration() != null ?
                absenceUpdateRequest.getDuration() : existing.getDuration());
        request.setDate(absenceUpdateRequest.getDate() != null ?
                absenceUpdateRequest.getDate() : existing.getDate());
        request.setEndDate(absenceUpdateRequest.getEndDate() != null ?
                absenceUpdateRequest.getEndDate() : existing.getEndDate());
        request.setReason(absenceUpdateRequest.getReason() != null ?
                absenceUpdateRequest.getReason() : existing.getReason());
        request.setDescription(absenceUpdateRequest.getDescription() != null ?
                absenceUpdateRequest.getDescription() : existing.getDescription());

        return request;
    }
}