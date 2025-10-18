package com.efms.employee_file_ms_be.command.salary_event;

import com.efms.employee_file_ms_be.api.request.SalaryEventCreateRequest;
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
import org.springframework.validation.annotation.Validated;

import java.time.format.DateTimeFormatter;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
@Validated
public class SalaryEventByAdvanceCreateCmd implements Command {

    @NotNull
    @Setter
    private Advance advance;

    @Getter
    private SalaryEvent salaryEvent;

    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        validateAdvance();

        SalaryEventCreateRequest salaryEventCreateRequest = buildSalaryEventCreateRequest(advance);

        salaryEvent = createSalaryEvent(salaryEventCreateRequest);
    }

    private void validateAdvance() {
        if (advance.getEmployee() == null || advance.getEmployee().getId() == null) {
            throw new CommonBadRequestException(Constants.ExceptionMessage.ADVANCE_EMPLOYEE_NOT_NULL);
        }
    }

    private SalaryEvent createSalaryEvent(SalaryEventCreateRequest request) {
        SalaryEventCreateCmd command = commandFactory.createCommand(SalaryEventCreateCmd.class);
        command.setSalaryEventCreateRequest(request);
        command.execute();
        return command.getSalaryEvent();
    }

    private SalaryEventCreateRequest buildSalaryEventCreateRequest(Advance advance) {
        SalaryEventCreateRequest request = new SalaryEventCreateRequest();
        request.setEmployeeId(advance.getEmployee().getId().toString());
        request.setType(SalaryEventType.DEDUCTION);
        request.setCategory(SalaryEventCategory.ADVANCE);
        request.setDescription(buildDeductionDescription(advance));
        request.setAmount(advance.getTotalAmount());
        request.setFrequency(SalaryEventFrequency.ONE_TIME);
        request.setStartDate(advance.getAdvanceDate());
        return request;
    }

    private String buildDeductionDescription(Advance advance) {
        return "Descuento por adelanto - " +
                advance.getAdvanceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " - " + advance.getPercentageAmount() + "%";
    }
}
