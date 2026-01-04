package com.efms.employee_file_ms_be.command.vacation;

import com.efms.employee_file_ms_be.api.request.VacationCreateRequest;
import com.efms.employee_file_ms_be.api.response.VacationResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Vacation;
import com.efms.employee_file_ms_be.model.mapper.VacationMapper;
import com.efms.employee_file_ms_be.model.repository.VacationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class VacationCreateCmd implements Command {

    @Setter
    private VacationCreateRequest vacationCreateRequest;

    @Getter
    private VacationResponse vacationResponse;

    @Getter
    private Vacation vacation;

    private final VacationRepository vacationRepository;

    private final VacationMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        vacation = mapper.toEntity(vacationCreateRequest);
        vacation.setCompanyId(companyId);
        vacation = vacationRepository.save(vacation);
        vacationResponse = mapper.toDTO(vacation);
    }
}
