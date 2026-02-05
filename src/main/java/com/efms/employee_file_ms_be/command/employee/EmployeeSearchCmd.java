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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeSearchCmd implements Command {

    @Setter
    private EmployeeSearchRequest searchRequest;

    @Getter
    private Page<Employee> employees;

    @Getter
    private Page<EmployeeResponse> employeesResponse;

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper mapper;

    @Override
    public void execute(){
        UUID companyId = Optional.ofNullable(searchRequest.getCompanyId())
                .orElseGet(() -> UUID.fromString(TenantContext.getTenantId()));

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
                companyId,
                searchRequest.getContractCompany()
        );

        employees = employeeRepository.findAll(spec, searchRequest.getPageable());
        employeesResponse = employees.map(mapper::toDTO);
    }
}
