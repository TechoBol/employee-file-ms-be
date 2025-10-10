package com.efms.employee_file_ms_be.command.branch;

import com.efms.employee_file_ms_be.api.request.BranchCreateRequest;
import com.efms.employee_file_ms_be.api.response.BranchResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Branch;
import com.efms.employee_file_ms_be.model.mapper.branch.BranchMapper;
import com.efms.employee_file_ms_be.model.repository.BranchRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class BranchCreateCmd implements Command {

    @Setter
    private BranchCreateRequest branchCreateRequest;

    @Getter
    private BranchResponse branchResponse;

    private final BranchRepository repository;

    private final BranchMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Branch branch = mapper.toEntity(branchCreateRequest);
        branch.setCompanyId(companyId);
        branch = repository.save(branch);
        branch.setCompanyId(companyId);
        branchResponse = mapper.toDTO(branch);
    }
}
