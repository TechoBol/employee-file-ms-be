package com.efms.employee_file_ms_be.command.branch;

import com.efms.employee_file_ms_be.api.request.BranchUpdateRequest;
import com.efms.employee_file_ms_be.api.response.BranchResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.BranchNotFoundException;
import com.efms.employee_file_ms_be.model.domain.Branch;
import com.efms.employee_file_ms_be.model.domain.Company;
import com.efms.employee_file_ms_be.model.mapper.branch.BranchMapper;
import com.efms.employee_file_ms_be.model.repository.BranchRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class BranchPatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private BranchUpdateRequest branchUpdateRequest;

    @Getter
    private BranchResponse branchResponse;

    private final BranchRepository repository;

    private final BranchMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Branch branch = repository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new BranchNotFoundException(id));
        updateProperties(branch, branchUpdateRequest);
        branch = repository.save(branch);
        branchResponse = mapper.toDTO(branch);
    }

    private void updateProperties(Branch branch, BranchUpdateRequest branchUpdateRequest) {
        Optional.ofNullable(branchUpdateRequest.getName()).ifPresent(branch::setName);
        Optional.ofNullable(branchUpdateRequest.getDescription()).ifPresent(branch::setDescription);
        Optional.ofNullable(branchUpdateRequest.getCity()).ifPresent(branch::setCity);
        Optional.ofNullable(branchUpdateRequest.getCountry()).ifPresent(branch::setCountry);
        Optional.ofNullable(branchUpdateRequest.getLocation()).ifPresent(branch::setLocation);
    }
}
