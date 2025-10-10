package com.efms.employee_file_ms_be.command.department;

import com.efms.employee_file_ms_be.api.response.DepartmentResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Department;
import com.efms.employee_file_ms_be.model.mapper.department.DepartmentMapper;
import com.efms.employee_file_ms_be.model.repository.DepartmentRepository;
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
public class DepartmentListCmd implements Command {

    @Getter
    private List<DepartmentResponse> departments;

    private final DepartmentRepository repository;

    private final DepartmentMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        List<Department> departmentsByCompany = repository.findAllByCompanyId(companyId);
        departments = departmentsByCompany.stream()
                .map(mapper::toDTO)
                .toList();
    }
}