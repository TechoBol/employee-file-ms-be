package com.efms.employee_file_ms_be.command.general_settings;

import com.efms.employee_file_ms_be.api.request.GeneralSettingsCreateRequest;
import com.efms.employee_file_ms_be.api.response.GeneralSettingsResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.GeneralSettings;
import com.efms.employee_file_ms_be.model.mapper.general_settings.GeneralSettingsMapper;
import com.efms.employee_file_ms_be.model.repository.GeneralSettingsRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class GeneralSettingsCreateCmd implements Command {

    @Setter
    private GeneralSettingsCreateRequest generalSettingsCreateRequest;

    @Getter
    private GeneralSettingsResponse generalSettingsResponse;

    private final GeneralSettingsRepository repository;

    private final GeneralSettingsMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        GeneralSettings generalSettings = mapper.toEntity(generalSettingsCreateRequest);
        generalSettings.setCompanyId(companyId);
        generalSettings = repository.save(generalSettings);
        generalSettingsResponse = mapper.toDTO(generalSettings);
    }
}
