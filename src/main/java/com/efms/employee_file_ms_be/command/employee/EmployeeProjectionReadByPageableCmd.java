package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import com.efms.employee_file_ms_be.model.repository.projection.EmployeeProjection;
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
public class EmployeeProjectionReadByPageableCmd implements Command {

    @Setter
    private Pageable pageable;

    @Getter
    private Page<EmployeeProjection> employeeProjectionPage;

    private final EmployeeRepository repository;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        employeeProjectionPage = repository.findAllProjectedByCompanyId(companyId, pageable);
    }
}
