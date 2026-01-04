package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.PayrollStatus;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.domain.SalaryEventCategory;
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
public class SalaryEventListByEmployeeIdCmd implements Command {

    @Setter
    private LocalDate startDate;

    @Setter
    private LocalDate endDate;

    @Setter
    private String employeeId;

    @Setter
    private SalaryEventCategory category;

    @Setter
    private Boolean useActualDate;

    @Getter
    private List<SalaryEventResponse> salaryEventResponseList;

    @Getter
    private List<SalaryEvent> salaryEventList;

    private final SalaryEventRepository repository;
    private final SalaryEventMapper mapper;

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

        salaryEventList = repository.findByEmployeeAndCompanyAndOptionalCategoryInDateRange(
                employeeUUID,
                companyId,
                statusToUse,
                category,
                startDate,
                endDate
        );

        salaryEventResponseList = salaryEventList.stream()
                .map(salaryEvent -> {
                    SalaryEventResponse response = mapper.toDTO(salaryEvent);
                    boolean isProcessed = salaryEvent.getStatus() != PayrollStatus.OPEN;
                    response.setProcessed(isProcessed);
                    return response;
                })
                .toList();
    }
}
