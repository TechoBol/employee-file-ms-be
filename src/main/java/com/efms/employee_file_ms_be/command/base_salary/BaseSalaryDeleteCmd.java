package com.efms.employee_file_ms_be.command.base_salary;

import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.repository.BaseSalaryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class BaseSalaryDeleteCmd implements Command {

    @Setter
    private String id;

    @Getter
    private int response;

    private final BaseSalaryRepository repository;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        response = repository.deleteByIdAndCompanyId(UUID.fromString(id), companyId);
    }
}
