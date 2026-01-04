package com.efms.employee_file_ms_be.model.mapper;

import com.efms.employee_file_ms_be.api.request.BranchCreateRequest;
import com.efms.employee_file_ms_be.api.response.BranchResponse;
import com.efms.employee_file_ms_be.model.domain.Branch;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author Josue Veliz
 */
@Component
public class BranchMapper implements CustomMapper<BranchResponse, BranchCreateRequest, Branch> {

    @Override
    public Branch toEntity(BranchCreateRequest branchCreateRequest) {
        Branch instance = new Branch();
        BeanUtils.copyProperties(branchCreateRequest, instance);

        return instance;
    }

    @Override
    public BranchResponse toDTO(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId().toString())
                .name(branch.getName())
                .description(branch.getDescription())
                .city(branch.getCity())
                .country(branch.getCountry())
                .location(branch.getLocation())
                .build();
    }
}

