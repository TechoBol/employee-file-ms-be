package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.repository.SalaryEventRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class SalaryEventDeleteByIdCmd implements Command {

    @Setter
    private UUID id;

    private final SalaryEventRepository salaryEventRepository;

    @Override
    public void execute() {
        salaryEventRepository.deleteById(id);
    }
}
