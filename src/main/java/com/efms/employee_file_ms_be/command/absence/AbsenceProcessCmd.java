package com.efms.employee_file_ms_be.command.absence;

import com.efms.employee_file_ms_be.api.response.payroll.ProcessedData;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Absence;
import com.efms.employee_file_ms_be.model.domain.PayrollStatus;
import com.efms.employee_file_ms_be.model.repository.AbsenceRepository;
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
public class AbsenceProcessCmd implements Command {

    @Setter
    private UUID companyId;

    @Setter
    private Pageable pageable;

    @Getter
    private List<ProcessedData> processedList;

    private final AbsenceRepository repository;

    @Override
    @Transactional
    public void execute() {
        Page<Absence> page = repository.findAllByCompanyId(companyId, null, pageable);

        if (page.isEmpty()) {
            processedList = List.of();
            return;
        }

        page.getContent().forEach(absence -> absence.setStatus(PayrollStatus.PROCESSED));
        repository.saveAll(page.getContent());

        processedList = page.getContent().stream().map(absence -> {
            ProcessedData dto = new ProcessedData();
            dto.setId(absence.getId());
            dto.setEmployeeId(absence.getEmployee().getId());
            dto.setDetails("Absence processed: " + absence.getType() + " - " + absence.getDuration());
            return dto;
        }).toList();
    }
}
