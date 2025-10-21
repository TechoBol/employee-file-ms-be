package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.response.payroll.ProcessedData;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.PayrollStatus;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.repository.SalaryEventRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@CommandExecute
@RequiredArgsConstructor
public class SalaryEventProcessCmd implements Command {

    @Setter
    private UUID companyId;

    @Setter
    private Pageable pageable;

    @Getter
    private List<ProcessedData> processedList = List.of();

    private final SalaryEventRepository repository;

    @Override
    @Transactional
    public void execute() {
        Page<SalaryEvent> page = repository.findAllByCompanyId(companyId, pageable);

        if (page.isEmpty()) {
            processedList = List.of();
            return;
        }

        page.getContent().forEach(event -> event.setStatus(PayrollStatus.PROCESSED));
        repository.saveAll(page.getContent());

        processedList = page.getContent().stream().map(event -> {
            ProcessedData dto = new ProcessedData();
            dto.setId(event.getId());
            dto.setEmployeeId(event.getEmployee().getId());
            dto.setDetails("Salary event processed: " + event.getCategory() + " - " + event.getAmount());
            return dto;
        }).toList();
    }
}
