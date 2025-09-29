package com.efms.employee_file_ms_be.command.department;

import com.efms.employee_file_ms_be.api.request.DepartmentUpdateRequest;
import com.efms.employee_file_ms_be.api.response.DepartmentResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Department;
import com.efms.employee_file_ms_be.model.mapper.department.DepartmentMapper;
import com.efms.employee_file_ms_be.model.repository.DepartmentRepository;
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
public class DepartmentPatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private DepartmentUpdateRequest departmentUpdateRequest;

    @Getter
    private DepartmentResponse departmentResponse;

    private final DepartmentRepository repository;

    private final DepartmentMapper mapper;

    @Override
    public void execute() {
        Department department = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Department not found"));
        updateProperties(department, departmentUpdateRequest);
        department = repository.save(department);
        departmentResponse = mapper.toDTO(department);
    }

    private void updateProperties(Department department, DepartmentUpdateRequest departmentUpdateRequest) {
        Optional.ofNullable(departmentUpdateRequest.getName()).ifPresent(department::setName);
    }
}
