package com.efms.employee_file_ms_be.command.branch;

import com.efms.employee_file_ms_be.api.response.BranchResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Branch;
import com.efms.employee_file_ms_be.model.mapper.BranchMapper;
import com.efms.employee_file_ms_be.model.repository.BranchRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class BranchListCmd implements Command {

    @Getter
    private List<BranchResponse> branches;

    private final BranchRepository repository;

    private final BranchMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        List<Branch> branchesByCompany = repository.findByCompanyId(companyId);
        branches = branchesByCompany.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
