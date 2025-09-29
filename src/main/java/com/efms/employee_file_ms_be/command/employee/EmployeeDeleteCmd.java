package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@CommandExecute
@RequiredArgsConstructor
public class EmployeeDeleteCmd implements Command {

    @Setter
    private String id;

    private final EmployeeRepository employeeRepository;

    @Override
    public void execute() {
        employeeRepository.deleteById(UUID.fromString(id));
    }
}
