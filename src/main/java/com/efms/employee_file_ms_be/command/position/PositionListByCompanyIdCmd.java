package com.efms.employee_file_ms_be.command.position;

import com.efms.employee_file_ms_be.api.response.PositionResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Position;
import com.efms.employee_file_ms_be.model.mapper.position.PositionMapper;
import com.efms.employee_file_ms_be.model.repository.PositionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class PositionListByCompanyIdCmd implements Command {

    @Setter
    private String companyId;

    @Getter
    private List<PositionResponse> positions;

    private final PositionRepository repository;

    private final PositionMapper mapper;

    @Override
    public void execute() {
        List<Position> positionsByCompany = repository.findPositionsByCompanyId(UUID.fromString(companyId));
        positions = positionsByCompany.stream()
                .map(mapper::toDTO)
                .toList();
    }
}

