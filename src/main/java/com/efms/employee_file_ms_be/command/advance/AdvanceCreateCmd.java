package com.efms.employee_file_ms_be.command.advance;

import com.efms.employee_file_ms_be.api.request.AdvanceCreateRequest;
import com.efms.employee_file_ms_be.api.response.AdvanceResponse;
import com.efms.employee_file_ms_be.api.response.BaseSalaryResponse;
import com.efms.employee_file_ms_be.command.base_salary.BaseSalaryReadByEmployeeIdCmd;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.salary_event.SalaryEventByAdvanceCreateCmd;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.CommonBadRequestException;
import com.efms.employee_file_ms_be.exception.Constants;
import com.efms.employee_file_ms_be.model.domain.Advance;
import com.efms.employee_file_ms_be.model.domain.SalaryEvent;
import com.efms.employee_file_ms_be.model.mapper.AdvanceMapper;
import com.efms.employee_file_ms_be.model.repository.AdvanceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class AdvanceCreateCmd implements Command {

    @Setter
    private AdvanceCreateRequest advanceCreateRequest;

    @Getter
    private AdvanceResponse advanceResponse;

    @Getter
    private Advance advance;

    private final AdvanceRepository advanceRepository;
    private final AdvanceMapper mapper;
    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        BaseSalaryResponse baseSalaryResponse = findBaseSalary(advanceCreateRequest.getEmployeeId());

        BigDecimal requestedAmount = advanceCreateRequest.getAmount();
        BigDecimal baseSalaryAmount = baseSalaryResponse.getAmount();

        if (requestedAmount.compareTo(baseSalaryAmount) > 0) {
            throw new CommonBadRequestException(Constants.ExceptionMessage.ADVANCE_PAYMENT_EXCEED);
        }

        advance = mapper.toEntity(advanceCreateRequest);
        advance.setCompanyId(companyId);
        advance.setAmount(requestedAmount);

        SalaryEvent salaryEvent = createSalaryEventForAdvance(advance);
        if (salaryEvent != null) {
            advance.setSalaryEvent(salaryEvent);
        }

        advance = advanceRepository.save(advance);
        advanceResponse = mapper.toDTO(advance);
    }

    private SalaryEvent createSalaryEventForAdvance(Advance advance) {
        SalaryEventByAdvanceCreateCmd command = commandFactory.createCommand(SalaryEventByAdvanceCreateCmd.class);
        command.setAdvance(advance);
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
