package com.efms.employee_file_ms_be.command.base_salary;

import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.BaseSalary;
import com.efms.employee_file_ms_be.model.mapper.base_salary.BaseSalaryMapper;
import com.efms.employee_file_ms_be.model.repository.BaseSalaryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class BaseSalaryReadByPageableCmd implements Command {

    @Setter
    private String companyId;

    @Setter
    private Pageable pageable;

    @Getter
    private Page<BaseSalaryResponse> baseSalaryResponse;

    private final BaseSalaryRepository repository;

    private final BaseSalaryMapper mapper;

    @Override
    public void execute() {
        Page<BaseSalary> baseSalaryPage = repository
                .findAllByCompanyIdAndActiveEmployees(UUID.fromString(companyId), pageable);
        baseSalaryResponse = baseSalaryPage.map(mapper::toDTO);
    }
}
