package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeSearchRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.mapper.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import com.efms.employee_file_ms_be.model.repository.specification.EmployeeSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

@CommandExecute
@RequiredArgsConstructor
public class EmployeeReadByPageableCmd implements Command {

    @Setter
    private Pageable pageable;

    @Setter
    private EmployeeSearchRequest searchRequest;

    @Getter
    private Page<EmployeeResponse> employees;

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Page<Employee> employeePage;

        if (hasSearchFilters()) {
            Specification<Employee> spec = EmployeeSpecification.withFilters(
                    searchRequest.getSearch(),
                    searchRequest.getCi(),
                    searchRequest.getEmail(),
                    searchRequest.getPhone(),
                    searchRequest.getType(),
                    searchRequest.getStatus(),
                    searchRequest.getIsDisassociated(),
                    searchRequest.getBranchId(),
                    searchRequest.getPositionId(),
                    companyId
            );
            employeePage = repository.findAll(spec, pageable);
        } else {
            employeePage = repository.findAllByCompanyId(companyId, pageable);
        }

        employees = employeePage.map(mapper::toDTO);
    }

    private boolean hasSearchFilters() {
        if (searchRequest == null) {
            return false;
        }

        return (searchRequest.getSearch() != null && !searchRequest.getSearch().isBlank())
                || (searchRequest.getCi() != null && !searchRequest.getCi().isBlank())
                || (searchRequest.getEmail() != null && !searchRequest.getEmail().isBlank())
                || (searchRequest.getPhone() != null && !searchRequest.getPhone().isBlank())
                || searchRequest.getType() != null
                || searchRequest.getStatus() != null
                || searchRequest.getIsDisassociated() != null
                || searchRequest.getBranchId() != null
                || searchRequest.getPositionId() != null;
    }
}
