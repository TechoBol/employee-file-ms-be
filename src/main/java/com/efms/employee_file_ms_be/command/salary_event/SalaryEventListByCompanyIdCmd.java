package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.response.SalaryEventResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.mapper.salary_event.SalaryEventMapper;
import com.efms.employee_file_ms_be.model.repository.SalaryEventRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class SalaryEventListByCompanyIdCmd implements Command {

    @Setter
    private String companyId;

    @Getter
    private List<SalaryEventResponse> salaryEventResponseList;

    @Getter
    private List<SalaryEvent> salaryEventList;

    private final SalaryEventRepository repository;

    private final SalaryEventMapper mapper;

    @Override
    public void execute() {
        salaryEventList = repository.findByEmployeeCompanyId(UUID.fromString(companyId));
        salaryEventResponseList = salaryEventList.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
