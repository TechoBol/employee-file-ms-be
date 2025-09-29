package com.efms.employee_file_ms_be.command.employee;


import com.efms.employee_file_ms_be.api.request.EmployeeCreateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.mapper.employee.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@CommandExecute
@RequiredArgsConstructor
public class EmployeeCreateCmd implements Command {

    @Setter
    private EmployeeCreateRequest employeeCreateRequest;

    @Getter
    private EmployeeResponse response;

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    @Override
    public void execute() {
        Employee employee = repository.save(mapper.toEntity(employeeCreateRequest));
        response = mapper.toDTO(employee);
    }
}
