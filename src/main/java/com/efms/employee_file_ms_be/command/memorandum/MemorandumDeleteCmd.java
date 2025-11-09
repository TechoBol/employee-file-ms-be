package com.efms.employee_file_ms_be.command.memorandum;

import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.repository.MemorandumRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class MemorandumDeleteCmd implements Command {

    @Setter
    private String id;

    @Getter
    private int response;

    private final MemorandumRepository memorandumRepository;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        response = memorandumRepository.deleteByIdAndCompanyId(UUID.fromString(id), companyId);
    }
}
