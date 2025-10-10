package com.efms.employee_file_ms_be.model.mapper.branch;

import com.efms.employee_file_ms_be.api.request.BranchCreateRequest;
import com.efms.employee_file_ms_be.api.response.BranchResponse;
import com.efms.employee_file_ms_be.model.domain.Branch;
import com.efms.employee_file_ms_be.model.domain.Company;
import com.efms.employee_file_ms_be.model.mapper.CustomMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Component
public class BranchMapper implements CustomMapper<BranchResponse, BranchCreateRequest, Branch> {

    @Override
    public Branch toEntity(BranchCreateRequest branchCreateRequest) {
        Branch instance = new Branch();
        instance.setName(branchCreateRequest.getName());

        return instance;
    }

    @Override
    public BranchResponse toDTO(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId().toString())
                .name(branch.getName())
                .companyId(String.valueOf(branch.getCompanyId()))
                .build();
    }
}

