package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.request.SalaryEventUpdateRequest;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.exception.CommonBadRequestException;
import com.efms.employee_file_ms_be.exception.Constants;
import com.efms.employee_file_ms_be.model.domain.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@CommandExecute
@RequiredArgsConstructor
public class SalaryEventByAdvancePatchCmd implements Command {

    @NotNull
    @Setter
    private Advance advance;

    @NotNull
    @Setter
    private SalaryEvent existingSalaryEvent;

    @Getter
    private SalaryEvent salaryEvent;

    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        validateAdvance();

        SalaryEventUpdateRequest salaryEventUpdateRequest = buildSalaryEventUpdateRequest(advance);

        salaryEvent = updateSalaryEvent(salaryEventUpdateRequest);
    }

    private void validateAdvance() {
        if (advance.getEmployee() == null || advance.getEmployee().getId() == null) {
            throw new CommonBadRequestException(Constants.ExceptionMessage.ADVANCE_EMPLOYEE_NOT_NULL);
        }
    }

    private SalaryEvent updateSalaryEvent(SalaryEventUpdateRequest request) {
        SalaryEventPatchCmd command = commandFactory.createCommand(SalaryEventPatchCmd.class);
        command.setId(String.valueOf(existingSalaryEvent.getId()));
        command.setSalaryEventUpdateRequest(request);
        command.execute();
        return command.getSalaryEvent();
    }

    private SalaryEventUpdateRequest buildSalaryEventUpdateRequest(Advance advance) {
        SalaryEventUpdateRequest request = new SalaryEventUpdateRequest();
        request.setType(SalaryEventType.DEDUCTION);
        request.setCategory(SalaryEventCategory.ADVANCE);
        request.setDescription(buildDeductionDescription(advance));
        request.setAmount(advance.getAmount());
        request.setFrequency(String.valueOf(SalaryEventFrequency.ONE_TIME));
        request.setStartDate(advance.getAdvanceDate());
        return request;
    }

    private String buildDeductionDescription(Advance advance) {
        return "Descuento por adelanto - " +
                advance.getAdvanceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " - " + advance.getAmount();
    }
}
