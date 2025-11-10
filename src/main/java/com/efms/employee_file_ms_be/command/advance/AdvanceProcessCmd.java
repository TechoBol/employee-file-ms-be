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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@CommandExecute
@RequiredArgsConstructor
public class AdvanceProcessCmd implements Command {

    @Setter
    private UUID companyId;

    @Setter
    private Pageable pageable;

    @Getter
    private List<ProcessedData> processedList;

    private final AdvanceRepository repository;

    @Override
    @Transactional
    public void execute() {
        Page<Advance> page = repository.findAllByCompanyId(companyId, null, pageable);

        if (page.isEmpty()) {
            processedList = List.of();
            return;
        }

        page.getContent().forEach(advance -> advance.setStatus(PayrollStatus.PROCESSED));
        repository.saveAll(page.getContent());

        processedList = page.getContent().stream().map(advance -> {
            ProcessedData dto = new ProcessedData();
            dto.setId(advance.getId());
            dto.setEmployeeId(advance.getEmployee().getId());
            dto.setDetails("Advance processed: " + advance.getAmount());
            return dto;
        }).toList();
    }
}
