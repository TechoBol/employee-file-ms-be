package com.efms.employee_file_ms_be.command.department;

import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.repository.DepartmentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class DepartmentDeleteCmd implements Command {

    @Setter
    private String id;

    @Getter
    private int response;

    private final DepartmentRepository repository;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        response = repository.deleteByIdAndCompanyId(UUID.fromString(id), companyId);
    }
}
