package com.efms.employee_file_ms_be.command.vacation;

import com.efms.employee_file_ms_be.api.response.VacationResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Vacation;
import com.efms.employee_file_ms_be.model.mapper.vacation.VacationMapper;
import com.efms.employee_file_ms_be.model.repository.VacationRepository;
import com.efms.employee_file_ms_be.util.DateUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class VacationListCmd implements Command {

    @Setter
    private LocalDate startDate;

    @Setter
    private LocalDate endDate;

    @Getter
    private List<VacationResponse> vacationResponseList;

    @Getter
    private List<Vacation> vacationList;

    private final VacationRepository repository;

    private final VacationMapper mapper;

    @Override
    public void execute() {
        startDate = DateUtils.getStartDateOrDefault(startDate);
        endDate = DateUtils.getEndDateOrDefault(endDate);
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        vacationList = repository.findByCompanyInDateRange(companyId, startDate, endDate);
        vacationResponseList = vacationList.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
