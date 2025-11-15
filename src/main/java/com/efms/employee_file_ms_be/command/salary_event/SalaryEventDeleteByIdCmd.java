package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
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
public class SalaryEventDeleteByIdCmd implements Command {

    @Setter
    private String id;

    @Getter
    private int response;

    private final SalaryEventRepository salaryEventRepository;

    @Transactional
    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        response = salaryEventRepository.deleteByIdAndCompanyId(UUID.fromString(id), companyId);
    }
}
