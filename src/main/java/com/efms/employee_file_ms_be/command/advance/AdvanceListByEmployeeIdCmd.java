package com.efms.employee_file_ms_be.command.advance;

import com.efms.employee_file_ms_be.api.response.AdvanceResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Advance;
import com.efms.employee_file_ms_be.model.domain.PayrollStatus;
import com.efms.employee_file_ms_be.model.mapper.advance.AdvanceMapper;
import com.efms.employee_file_ms_be.model.repository.AdvanceRepository;
import com.efms.employee_file_ms_be.util.DateUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.efms.employee_file_ms_be.util.DateUtils.*;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class AdvanceListByEmployeeIdCmd implements Command {

    @Setter
    private LocalDate startDate;

    @Setter
    private LocalDate endDate;

    @Setter
    private String employeeId;

    @Getter
    private List<Advance> advanceList;

    @Getter
    private List<AdvanceResponse> advanceResponseList;

    private final AdvanceRepository repository;
    private final AdvanceMapper mapper;

    @Override
    public void execute() {
        startDate = getStartDateOrDefault(startDate);
        endDate = getEndDateOrDefault(endDate);
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        UUID employeeUUID = UUID.fromString(employeeId);

        PayrollStatus statusToUse = shouldSearchIgnoringStatus(startDate, endDate) ? null : PayrollStatus.PROCESSED;

        advanceList = repository.findByEmployeeAndCompanyInDateRange(
                employeeUUID,
                companyId,
                statusToUse,
                startDate,
                endDate
        );

        advanceResponseList = advanceList.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
