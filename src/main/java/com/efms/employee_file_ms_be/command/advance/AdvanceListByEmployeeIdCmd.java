package com.efms.employee_file_ms_be.command.advance;

import com.efms.employee_file_ms_be.api.response.AbsenceResponse;
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

    @Setter
    private Boolean useActualDate;

    @Getter
    private List<Advance> advanceList;

    @Getter
    private List<AdvanceResponse> advanceResponseList;

    private final AdvanceRepository repository;
    private final AdvanceMapper mapper;

    @Override
    public void execute() {
        if (Boolean.TRUE.equals(useActualDate)) {
            LocalDate today = LocalDate.now();
            startDate = today.withDayOfMonth(1);
            endDate = today;
        } else {
            startDate = DateUtils.getStartDateOrDefault(startDate);
            endDate = DateUtils.getEndDateOrDefault(endDate);
        }
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        UUID employeeUUID = UUID.fromString(employeeId);


        PayrollStatus statusToUse = shouldSearchIgnoringStatus(startDate, endDate)
                ? PayrollStatus.OPEN
                : null;
        // use PayrollStatus.PROCESSED instead of null if you want to ignore.

        advanceList = repository.findByEmployeeAndCompanyInDateRange(
                employeeUUID,
                companyId,
                statusToUse,
                startDate,
                endDate
        );

        advanceResponseList = advanceList.stream()
                .map(advance -> {
                    AdvanceResponse response = mapper.toDTO(advance);
                    boolean isProcessed = advance.getStatus() != PayrollStatus.OPEN;
                    response.setProcessed(isProcessed);
                    return response;
                })
                .toList();
    }
}
