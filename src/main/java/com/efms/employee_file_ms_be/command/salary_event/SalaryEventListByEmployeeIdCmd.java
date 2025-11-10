package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.PayrollStatus;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.domain.SalaryEventCategory;
import com.efms.employee_file_ms_be.model.mapper.salary_event.SalaryEventMapper;
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
    private String category;

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
        UUID employeeUUID = UUID.fromString(employeeId);

        SalaryEventCategory categoryEnum = (category != null && !category.isBlank())
                ? SalaryEventCategory.valueOf(category.toUpperCase())
                : null;

        PayrollStatus statusToUse = shouldSearchIgnoringStatus(startDate, endDate) ? null : PayrollStatus.PROCESSED;

        salaryEventList = repository.findByEmployeeAndCompanyAndOptionalCategoryInDateRange(
                employeeUUID,
                companyId,
                statusToUse,
                categoryEnum,
                startDate,
                endDate
        );

        salaryEventResponseList = salaryEventList.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
