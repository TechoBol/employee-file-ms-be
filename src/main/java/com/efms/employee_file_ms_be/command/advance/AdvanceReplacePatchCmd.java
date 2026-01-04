package com.efms.employee_file_ms_be.command.advance;

import com.efms.employee_file_ms_be.api.request.AdvanceCreateRequest;
import com.efms.employee_file_ms_be.api.request.AdvanceUpdateRequest;
import com.efms.employee_file_ms_be.api.response.AdvanceResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.AdvanceNotFoundException;
import com.efms.employee_file_ms_be.model.domain.Advance;
import com.efms.employee_file_ms_be.model.repository.AdvanceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class AdvanceReplacePatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private AdvanceUpdateRequest advanceUpdateRequest;

    @Getter
    private AdvanceResponse advanceResponse;

    private final AdvanceRepository repository;
    private final CommandFactory commandFactory;

    @Transactional
    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Advance existingAdvance = repository.findByIdAndCompanyId(UUID.fromString(id), companyId, null)
                .orElseThrow(() -> new AdvanceNotFoundException(id));

        AdvanceCreateRequest createRequest = buildCreateRequest(existingAdvance);

        AdvanceDeleteCmd deleteCmd = commandFactory.createCommand(AdvanceDeleteCmd.class);
        deleteCmd.setId(id);
        deleteCmd.execute();

        AdvanceCreateCmd createCmd = commandFactory.createCommand(AdvanceCreateCmd.class);
        createCmd.setAdvanceCreateRequest(createRequest);
        createCmd.execute();

        advanceResponse = createCmd.getAdvanceResponse();
    }

    private AdvanceCreateRequest buildCreateRequest(Advance existing) {
        AdvanceCreateRequest request = new AdvanceCreateRequest();

        request.setEmployeeId(existing.getEmployee().getId().toString());
        request.setAmount(advanceUpdateRequest.getAmount() != null ?
                advanceUpdateRequest.getAmount() : existing.getAmount());
        request.setAdvanceDate(advanceUpdateRequest.getAdvanceDate() != null ?
                advanceUpdateRequest.getAdvanceDate() : existing.getAdvanceDate());

        return request;
    }
}
