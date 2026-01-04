package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.request.SalaryEventCreateRequest;
import com.efms.employee_file_ms_be.api.request.SalaryEventUpdateRequest;
import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.SalaryEventNotFound;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.domain.SalaryEventCategory;
import com.efms.employee_file_ms_be.model.domain.SalaryEventFrequency;
import com.efms.employee_file_ms_be.model.mapper.salary_event.SalaryEventMapper;
import com.efms.employee_file_ms_be.model.repository.SalaryEventRepository;
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
public class SalaryEventReplacePatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private SalaryEventUpdateRequest salaryEventUpdateRequest;

    @Getter
    private SalaryEventResponse salaryEventResponse;

    private final SalaryEventRepository repository;
    private final CommandFactory commandFactory;

    @Transactional
    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        SalaryEvent existingSalaryEvent = repository.findByIdAndCompanyId(UUID.fromString(id), companyId, null)
                .orElseThrow(() -> new SalaryEventNotFound(id));

        SalaryEventCreateRequest createRequest = buildCreateRequest(existingSalaryEvent);

        SalaryEventDeleteByIdCmd deleteCmd = commandFactory.createCommand(SalaryEventDeleteByIdCmd.class);
        deleteCmd.setId(id);
        deleteCmd.execute();

        SalaryEventCreateCmd createCmd = commandFactory.createCommand(SalaryEventCreateCmd.class);
        createCmd.setSalaryEventCreateRequest(createRequest);
        createCmd.execute();

        salaryEventResponse = createCmd.getSalaryEventResponse();
    }

    private SalaryEventCreateRequest buildCreateRequest(SalaryEvent existing) {
        SalaryEventCreateRequest request = new SalaryEventCreateRequest();

        request.setEmployeeId(existing.getEmployee().getId().toString());
        request.setType(salaryEventUpdateRequest.getType() != null ?
                salaryEventUpdateRequest.getType() : existing.getType());
        request.setCategory(salaryEventUpdateRequest.getCategory() != null ?
                salaryEventUpdateRequest.getCategory() :
                (existing.getCategory() != null ? existing.getCategory() : SalaryEventCategory.MANUAL));
        request.setDescription(salaryEventUpdateRequest.getDescription() != null ?
                salaryEventUpdateRequest.getDescription() : existing.getDescription());
        request.setAmount(salaryEventUpdateRequest.getAmount() != null ?
                salaryEventUpdateRequest.getAmount() : existing.getAmount());
        request.setFrequency(salaryEventUpdateRequest.getFrequency() != null ?
                SalaryEventFrequency.valueOf(salaryEventUpdateRequest.getFrequency()) : existing.getFrequency());
        request.setStartDate(salaryEventUpdateRequest.getStartDate() != null ?
                salaryEventUpdateRequest.getStartDate() : existing.getStartDate());
        request.setEndDate(salaryEventUpdateRequest.getEndDate() != null ?
                salaryEventUpdateRequest.getEndDate() : existing.getEndDate());

        return request;
    }
}
