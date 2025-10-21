package com.efms.employee_file_ms_be.command.memorandum;

import com.efms.employee_file_ms_be.api.request.MemorandumCreateRequest;
import com.efms.employee_file_ms_be.api.response.MemorandumResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Memorandum;
import com.efms.employee_file_ms_be.model.mapper.memorandum.MemorandumMapper;
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
public class MemorandumCreateCmd implements Command {

    @Setter
    private MemorandumCreateRequest memorandumCreateRequest;

    @Getter
    private MemorandumResponse memorandumResponse;

    @Getter
    private Memorandum memorandum;

    private final MemorandumRepository memorandumRepository;

    private final MemorandumMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        memorandum = mapper.toEntity(memorandumCreateRequest);
        memorandum.setCompanyId(companyId);
        memorandum = memorandumRepository.save(memorandum);
        memorandumResponse = mapper.toDTO(memorandum);
    }
}
