package com.efms.employee_file_ms_be.command.position;

import com.efms.employee_file_ms_be.api.response.PositionResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Position;
import com.efms.employee_file_ms_be.model.mapper.position.PositionMapper;
import com.efms.employee_file_ms_be.model.repository.PositionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class PositionListCmd implements Command {

    @Getter
    private List<PositionResponse> positions;

    private final PositionRepository repository;

    private final PositionMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        List<Position> positionsByCompany = repository.findAllByCompanyId(companyId);
        positions = positionsByCompany.stream()
                .map(mapper::toDTO)
                .toList();
    }
}

