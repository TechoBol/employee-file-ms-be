package com.efms.employee_file_ms_be.command.position;

import com.efms.employee_file_ms_be.api.request.PositionCreateRequest;
import com.efms.employee_file_ms_be.api.response.PositionResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.model.domain.Position;
import com.efms.employee_file_ms_be.model.mapper.position.PositionMapper;
import com.efms.employee_file_ms_be.model.repository.PositionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class PositionCreateCmd implements Command {

    @Setter
    private PositionCreateRequest positionCreateRequest;

    @Getter
    private PositionResponse positionResponse;

    private final PositionRepository repository;

    private final PositionMapper mapper;

    @Override
    public void execute() {
        Position position = repository.save(mapper.toEntity(positionCreateRequest));
        positionResponse = mapper.toDTO(position);
    }
}
