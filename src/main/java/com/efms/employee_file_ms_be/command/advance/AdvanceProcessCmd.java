package com.efms.employee_file_ms_be.command.advance;

import com.efms.employee_file_ms_be.api.response.payroll.ProcessedData;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Advance;
import com.efms.employee_file_ms_be.model.domain.PayrollStatus;
import com.efms.employee_file_ms_be.model.repository.AdvanceRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.efms.employee_file_ms_be.util.DateUtils.getStartDateFromPeriod;
import static com.efms.employee_file_ms_be.util.DateUtils.getEndDateFromPeriod;

@CommandExecute
@RequiredArgsConstructor
public class AdvanceProcessCmd implements Command {

    @Setter
    private UUID companyId;

    @Setter
    private Integer period;

    @Getter
    private List<ProcessedData> processedList;

    private final AdvanceRepository repository;

    @Override
    @Transactional
    public void execute() {
        LocalDate startDate = getStartDateFromPeriod(period);
        LocalDate endDate = getEndDateFromPeriod(period);
        List<Advance> advances = repository.findByCompanyInDateRange(companyId, PayrollStatus.OPEN, startDate, endDate);

        if (advances.isEmpty()) {
            processedList = List.of();
            return;
        }

        advances.forEach(advance -> advance.setStatus(PayrollStatus.PROCESSED));
        repository.saveAll(advances);

        processedList = advances.stream().map(advance -> {
            ProcessedData dto = new ProcessedData();
            dto.setId(advance.getId());
            dto.setEmployeeId(advance.getEmployee().getId());
            dto.setDetails("Advance processed: " + advance.getAmount());
            return dto;
        }).toList();
    }
}
