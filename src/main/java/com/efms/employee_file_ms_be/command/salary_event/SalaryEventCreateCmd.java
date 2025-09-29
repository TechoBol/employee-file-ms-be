package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.request.SalaryEventCreateRequest;
import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.mapper.salary_event.SalaryEventMapper;
import com.efms.employee_file_ms_be.model.repository.SalaryEventRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@CommandExecute
@RequiredArgsConstructor
public class SalaryEventCreateCmd implements Command {

    @Setter
    private SalaryEventCreateRequest salaryEventCreateRequest;

    @Getter
    private SalaryEventResponse salaryEventResponse;

    @Getter
    private SalaryEvent salaryEvent;

    private final SalaryEventRepository repository;

    private final SalaryEventMapper mapper;

    @Override
    public void execute() {
        salaryEvent = repository.save(mapper.toEntity(salaryEventCreateRequest));
        salaryEventResponse = mapper.toDTO(salaryEvent);
    }
}

