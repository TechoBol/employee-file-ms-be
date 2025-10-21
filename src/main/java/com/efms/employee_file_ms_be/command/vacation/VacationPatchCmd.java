package com.efms.employee_file_ms_be.command.vacation;

import com.efms.employee_file_ms_be.api.request.VacationUpdateRequest;
import com.efms.employee_file_ms_be.api.response.VacationResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.RecordEditNotAllowedException;
import com.efms.employee_file_ms_be.exception.VacationNotFoundException;
import com.efms.employee_file_ms_be.model.domain.Vacation;
import com.efms.employee_file_ms_be.model.mapper.vacation.VacationMapper;
import com.efms.employee_file_ms_be.model.repository.VacationRepository;
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
public class VacationPatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private VacationUpdateRequest vacationUpdateRequest;

    @Getter
    private VacationResponse vacationResponse;

    private final VacationRepository repository;

    private final VacationMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Vacation vacation = repository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new VacationNotFoundException(id));

        validateEditingTimeframe(vacation);

        updateProperties(vacation, vacationUpdateRequest);
        vacation = repository.save(vacation);
        vacationResponse = mapper.toDTO(vacation);
    }

    private void validateEditingTimeframe(Vacation vacation) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = vacation.getStartDate();

        LocalDate lastDayOfVacationMonth = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDate fifthDayOfNextMonth = lastDayOfVacationMonth.plusDays(5);

        if (now.isAfter(fifthDayOfNextMonth)) {
            throw new RecordEditNotAllowedException();
        }
    }

    private void updateProperties(Vacation vacation, VacationUpdateRequest vacationUpdateRequest) {
        Optional.ofNullable(vacationUpdateRequest.getStartDate()).ifPresent(vacation::setStartDate);
        Optional.ofNullable(vacationUpdateRequest.getEndDate()).ifPresent(vacation::setEndDate);
        Optional.ofNullable(vacationUpdateRequest.getNotes()).ifPresent(vacation::setNotes);
    }
}
