package com.efms.employee_file_ms_be.command.advance;

import com.efms.employee_file_ms_be.api.request.AdvanceUpdateRequest;
import com.efms.employee_file_ms_be.api.response.AdvanceResponse;
import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.base_salary.BaseSalaryReadByEmployeeIdCmd;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventByAdvancePatchCmd;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.AdvanceNotFoundException;
import com.efms.employee_file_ms_be.exception.RecordEditNotAllowedException;
import com.efms.employee_file_ms_be.model.domain.Advance;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.mapper.advance.AdvanceMapper;
import com.efms.employee_file_ms_be.model.repository.AdvanceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.efms.employee_file_ms_be.command.Constants.MAX_DAYS_TO_EDIT;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class AdvancePatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private AdvanceUpdateRequest advanceUpdateRequest;

    @Getter
    private AdvanceResponse advanceResponse;

    private final AdvanceRepository repository;
    private final AdvanceMapper mapper;
    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Advance advance = repository.findByIdAndCompanyId(UUID.fromString(id), companyId, null)
                .orElseThrow(() -> new AdvanceNotFoundException(id));
        BaseSalaryResponse baseSalaryResponse = findBaseSalary(advance.getEmployee().getId().toString());
        BigDecimal totalAmount = baseSalaryResponse.getAmount().multiply(advanceUpdateRequest.getAmount());

        validateEditingTimeframe(advance);

        updateProperties(advance, advanceUpdateRequest);

        advance.setAmount(totalAmount);

        handleSalaryEventUpdate(advance);

        advance = repository.save(advance);
        advanceResponse = mapper.toDTO(advance);
    }

    private void validateEditingTimeframe(Advance advance) {
        LocalDate now = LocalDate.now();
        LocalDate advanceDate = advance.getAdvanceDate();

        LocalDate lastDayOfAdvanceMonth = advanceDate.withDayOfMonth(advanceDate.lengthOfMonth());

        LocalDate fifthDayOfNextMonth = lastDayOfAdvanceMonth.plusDays(MAX_DAYS_TO_EDIT);

        if (now.isAfter(fifthDayOfNextMonth)) {
            throw new RecordEditNotAllowedException();
        }
    }

    private void updateProperties(Advance advance, AdvanceUpdateRequest request) {
        Optional.ofNullable(request.getAmount()).ifPresent(advance::setAmount);
        Optional.ofNullable(request.getAdvanceDate()).ifPresent(advance::setAdvanceDate);
    }

    private void handleSalaryEventUpdate(Advance advance) {
        boolean hadSalaryEvent = advance.getSalaryEvent() != null;

        if (hadSalaryEvent && hasRelevantChanges()) {
            SalaryEvent updatedSalaryEvent = updateSalaryEvent(advance, advance.getSalaryEvent());
            advance.setSalaryEvent(updatedSalaryEvent);
        }
    }

    private boolean hasRelevantChanges() {
        return advanceUpdateRequest.getAmount() != null ||
                advanceUpdateRequest.getAdvanceDate() != null;
    }

    private SalaryEvent updateSalaryEvent(Advance advance, SalaryEvent existingSalaryEvent) {
        SalaryEventByAdvancePatchCmd command = commandFactory.createCommand(SalaryEventByAdvancePatchCmd.class);
        command.setAdvance(advance);
        command.setExistingSalaryEvent(existingSalaryEvent);
        command.execute();
        return command.getSalaryEvent();
    }

    private BaseSalaryResponse findBaseSalary(String employeeId) {
        BaseSalaryReadByEmployeeIdCmd command = commandFactory.createCommand(BaseSalaryReadByEmployeeIdCmd.class);
        command.setEmployeeId(employeeId);
        command.execute();
        return command.getBaseSalaryResponse();
    }
}
