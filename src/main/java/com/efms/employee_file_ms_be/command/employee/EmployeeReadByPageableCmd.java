package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.mapper.employee.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@CommandExecute
@RequiredArgsConstructor
public class EmployeeReadByPageableCmd implements Command {

    @Setter
    private String companyId;

    @Setter
    private Pageable pageable;

    @Getter
    private Page<EmployeeResponse> employees;

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    @Override
    public void execute() {
        Page<Employee> employeePage = repository.findAllByCompanyId(UUID.fromString(companyId), pageable);
        employees = employeePage.map(mapper::toDTO);
    }
}
