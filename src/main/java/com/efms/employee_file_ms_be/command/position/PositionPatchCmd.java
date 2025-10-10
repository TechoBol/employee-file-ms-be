package com.efms.employee_file_ms_be.command.position;

import com.efms.employee_file_ms_be.api.request.PositionUpdateRequest;
import com.efms.employee_file_ms_be.api.response.PositionResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.PositionNotFound;
import com.efms.employee_file_ms_be.model.domain.Position;
import com.efms.employee_file_ms_be.model.mapper.position.PositionMapper;
import com.efms.employee_file_ms_be.model.repository.PositionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class PositionPatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private PositionUpdateRequest positionUpdateRequest;

    @Getter
    private PositionResponse positionResponse;

    private final PositionRepository repository;

    private final PositionMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Position position = repository.findByIdAndCompanyId((UUID.fromString(id)), companyId)
                .orElseThrow(() -> new PositionNotFound(id));
        updateProperties(position, positionUpdateRequest);
        position = repository.save(position);
        positionResponse = mapper.toDTO(position);
    }

    private void updateProperties(Position position, PositionUpdateRequest positionUpdateRequest) {
        Optional.ofNullable(positionUpdateRequest.getName()).ifPresent(position::setName);
    }
}
