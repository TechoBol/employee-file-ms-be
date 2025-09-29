package com.efms.employee_file_ms_be.command.base_salary;

import com.efms.employee_file_ms_be.api.request.BaseSalaryCreateRequest;
import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.BaseSalary;
import com.efms.employee_file_ms_be.model.mapper.base_salary.BaseSalaryMapper;
import com.efms.employee_file_ms_be.model.repository.BaseSalaryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@CommandExecute
@RequiredArgsConstructor
public class BaseSalaryCreateCmd implements Command {

    @Setter
    private BaseSalaryCreateRequest baseSalaryCreateRequest;

    @Getter
    private BaseSalaryResponse baseSalaryResponse;

    private final BaseSalaryRepository repository;

    private final BaseSalaryMapper mapper;

    @Override
    public void execute() {
        BaseSalary baseSalary = repository.save(mapper.toEntity(baseSalaryCreateRequest));
        baseSalaryResponse = mapper.toDTO(baseSalary);
    }
}
