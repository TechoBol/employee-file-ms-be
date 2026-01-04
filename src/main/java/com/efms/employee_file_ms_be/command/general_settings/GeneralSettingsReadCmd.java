package com.efms.employee_file_ms_be.command.general_settings;

import com.efms.employee_file_ms_be.api.request.GeneralSettingsCreateRequest;
import com.efms.employee_file_ms_be.api.response.GeneralSettingsResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.config.GeneralSettingsProperties;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.GeneralSettings;
import com.efms.employee_file_ms_be.model.mapper.GeneralSettingsMapper;
import com.efms.employee_file_ms_be.model.repository.GeneralSettingsRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class GeneralSettingsReadCmd implements Command {

    @Getter
    private GeneralSettingsResponse generalSettingsResponse;

    @Getter
    private GeneralSettings generalSettings;

    private final GeneralSettingsProperties properties;

    private final GeneralSettingsRepository repository;

    private final CommandFactory commandFactory;

    private final GeneralSettingsMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        generalSettings = repository.findByCompanyId(companyId)
                .orElseGet(this::createDefaultSettings);
        generalSettingsResponse = mapper.toDTO(generalSettings);
    }

    private GeneralSettings createDefaultSettings() {
        GeneralSettingsCreateCmd cmd = commandFactory.createCommand(GeneralSettingsCreateCmd.class);

        GeneralSettingsCreateRequest createRequest = new GeneralSettingsCreateRequest();
        BeanUtils.copyProperties(properties, createRequest);

        cmd.setGeneralSettingsCreateRequest(createRequest);
        cmd.execute();

        return cmd.getGeneralSettings();
    }
}
