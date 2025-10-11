package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.request.SalaryEventUpdateRequest;
import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.SalaryEventNotFound;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.domain.SalaryEventFrequency;
import com.efms.employee_file_ms_be.model.mapper.salary_event.SalaryEventMapper;
import com.efms.employee_file_ms_be.model.repository.SalaryEventRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class SalaryEventPatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private SalaryEventUpdateRequest salaryEventUpdateRequest;

    @Getter
    private SalaryEventResponse salaryEventResponse;

    private final SalaryEventRepository repository;

    private final SalaryEventMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        SalaryEvent salaryEvent = repository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new SalaryEventNotFound(id));
        updateProperties(salaryEvent, salaryEventUpdateRequest);
        salaryEvent = repository.save(salaryEvent);
        salaryEventResponse = mapper.toDTO(salaryEvent);
    }

    private void updateProperties(SalaryEvent salaryEvent, SalaryEventUpdateRequest salaryEventUpdateRequest) {
        Optional.ofNullable(salaryEventUpdateRequest.getType()).ifPresent(salaryEvent::setType);
        Optional.ofNullable(salaryEventUpdateRequest.getDescription()).ifPresent(salaryEvent::setDescription);
        Optional.ofNullable(salaryEventUpdateRequest.getAmount()).ifPresent(salaryEvent::setAmount);
        Optional.ofNullable(salaryEventUpdateRequest.getFrequency()).ifPresent(SalaryEventFrequency::valueOf);
        Optional.ofNullable(salaryEventUpdateRequest.getStartDate()).ifPresent(salaryEvent::setStartDate);
        Optional.ofNullable(salaryEventUpdateRequest.getEndDate()).ifPresent(salaryEvent::setEndDate);
    }
}
