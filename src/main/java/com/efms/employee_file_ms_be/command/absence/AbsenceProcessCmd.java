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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.efms.employee_file_ms_be.util.DateUtils.getEndDateFromPeriod;
import static com.efms.employee_file_ms_be.util.DateUtils.getStartDateFromPeriod;

@CommandExecute
@RequiredArgsConstructor
public class AbsenceProcessCmd implements Command {

    @Setter
    private UUID companyId;

    @Setter
    private Integer period;

    @Getter
    private List<ProcessedData> processedList;

    private final AbsenceRepository repository;

    @Override
    @Transactional
    public void execute() {
        LocalDate startDate = getStartDateFromPeriod(period);
        LocalDate endDate = getEndDateFromPeriod(period);
        List<Absence> absences = repository.findByCompanyInDateRange(companyId, PayrollStatus.OPEN, startDate, endDate);

        if (absences.isEmpty()) {
            processedList = List.of();
            return;
        }

        absences.forEach(absence -> absence.setStatus(PayrollStatus.PROCESSED));
        repository.saveAll(absences);

        processedList = absences.stream().map(absence -> {
            ProcessedData dto = new ProcessedData();
            dto.setId(absence.getId());
            dto.setEmployeeId(absence.getEmployee().getId());
            dto.setDetails("Absence processed: " + absence.getType() + " - " + absence.getDuration());
            return dto;
        }).toList();
    }
}
