package com.efms.employee_file_ms_be.command.branch;

import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.repository.BranchRepository;
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
public class BranchDeleteCmd implements Command {

    @Setter
    private String id;

    @Getter
    private int response;

    private final BranchRepository repository;

    @Transactional
    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        response = repository.deleteByIdAndCompanyId(UUID.fromString(id), companyId);
    }
}
