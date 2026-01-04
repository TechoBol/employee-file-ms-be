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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.efms.employee_file_ms_be.util.DateUtils.getEndDateFromPeriod;
import static com.efms.employee_file_ms_be.util.DateUtils.getStartDateFromPeriod;

@CommandExecute
@RequiredArgsConstructor
public class SalaryEventProcessCmd implements Command {

    @Setter
    private UUID companyId;

    @Setter
    private Integer period;

    @Getter
    private List<ProcessedData> processedList = List.of();

    private final SalaryEventRepository repository;

    @Override
    @Transactional
    public void execute() {
        LocalDate startDate = getStartDateFromPeriod(period);
        LocalDate endDate = getEndDateFromPeriod(period);
        List<SalaryEvent> salaryEvents = repository.findByCompanyInDateRange(companyId, PayrollStatus.OPEN, startDate, endDate);

        if (salaryEvents.isEmpty()) {
            processedList = List.of();
            return;
        }

        salaryEvents.forEach(event -> event.setStatus(PayrollStatus.PROCESSED));
        repository.saveAll(salaryEvents);

        processedList = salaryEvents.stream().map(event -> {
            ProcessedData dto = new ProcessedData();
            dto.setId(event.getId());
            dto.setEmployeeId(event.getEmployee().getId());
            dto.setDetails("Salary event processed: " + event.getCategory() + " - " + event.getAmount());
            return dto;
        }).toList();
    }
}
