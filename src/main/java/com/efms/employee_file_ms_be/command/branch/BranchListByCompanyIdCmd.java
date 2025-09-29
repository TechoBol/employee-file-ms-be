package com.efms.employee_file_ms_be.command.branch;

import com.efms.employee_file_ms_be.api.response.BranchResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Branch;
import com.efms.employee_file_ms_be.model.mapper.branch.BranchMapper;
import com.efms.employee_file_ms_be.model.repository.BranchRepository;
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
public class BranchListByCompanyIdCmd implements Command {

    @Setter
    private String companyId;

    @Getter
    private List<BranchResponse> branches;

    private final BranchRepository repository;

    private final BranchMapper mapper;

    @Override
    public void execute() {
        List<Branch> branchesByCompany = repository.findBranchesByCompanyId(UUID.fromString(companyId));
        branches = branchesByCompany.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
