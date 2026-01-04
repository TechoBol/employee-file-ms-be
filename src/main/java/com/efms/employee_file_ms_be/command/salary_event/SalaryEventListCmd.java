package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.PayrollStatus;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.mapper.SalaryEventMapper;
import com.efms.employee_file_ms_be.model.repository.SalaryEventRepository;
import com.efms.employee_file_ms_be.util.DateUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.efms.employee_file_ms_be.util.DateUtils.shouldSearchIgnoringStatus;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class SalaryEventListCmd implements Command {

    @Setter
    private LocalDate startDate;

    @Setter
    private LocalDate endDate;

    @Getter
    private List<SalaryEventResponse> salaryEventResponseList;

    @Getter
    private List<SalaryEvent> salaryEventList;

    private final SalaryEventRepository repository;
    private final SalaryEventMapper mapper;

    @Override
    public void execute() {
        startDate = DateUtils.getStartDateOrDefault(startDate);
        endDate = DateUtils.getEndDateOrDefault(endDate);
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        PayrollStatus statusToUse = shouldSearchIgnoringStatus(startDate, endDate) ? PayrollStatus.OPEN : PayrollStatus.PROCESSED;

        salaryEventList = repository.findByCompanyInDateRange(
                companyId,
                statusToUse,
                startDate,
                endDate
        );

        salaryEventResponseList = salaryEventList.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
