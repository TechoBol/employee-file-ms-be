package com.efms.employee_file_ms_be.command.position;

import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.repository.PositionRepository;
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
public class PositionDeleteCmd implements Command {

    @Setter
    private String id;

    @Getter
    private int response;

    private final PositionRepository positionRepository;

    @Transactional
    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        response = positionRepository.deleteByIdAndCompanyId(UUID.fromString(id), companyId);
    }
}
