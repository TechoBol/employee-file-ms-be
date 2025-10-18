package com.efms.employee_file_ms_be.command.general_settings;

import com.efms.employee_file_ms_be.api.request.GeneralSettingsUpdateRequest;
import com.efms.employee_file_ms_be.api.response.GeneralSettingsResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.GeneralSettingsNotFoundException;
import com.efms.employee_file_ms_be.model.domain.Company;
import com.efms.employee_file_ms_be.model.domain.GeneralSettings;
import com.efms.employee_file_ms_be.model.mapper.general_settings.GeneralSettingsMapper;
import com.efms.employee_file_ms_be.model.repository.GeneralSettingsRepository;
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
public class GeneralSettingsPatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private GeneralSettingsUpdateRequest generalSettingsUpdateRequest;

    @Getter
    private GeneralSettingsResponse generalSettingsResponse;

    private final GeneralSettingsRepository repository;

    private final GeneralSettingsMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        GeneralSettings generalSettings = repository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new GeneralSettingsNotFoundException(id));
        updateProperties(generalSettings, generalSettingsUpdateRequest);
        generalSettings = repository.save(generalSettings);
        generalSettingsResponse = mapper.toDTO(generalSettings);
    }

    private void updateProperties(GeneralSettings generalSettings, GeneralSettingsUpdateRequest generalSettingsUpdateRequest) {
        Optional.ofNullable(generalSettingsUpdateRequest.getWorkingDaysPerMonth())
                .ifPresent(generalSettings::setWorkingDaysPerMonth);
        Optional.ofNullable(generalSettingsUpdateRequest.getSeniorityIncreasePercentage())
                .ifPresent(generalSettings::setSeniorityIncreasePercentage);
        Optional.ofNullable(generalSettingsUpdateRequest.getContributionAfpPercentage())
                .ifPresent(generalSettings::setContributionAfpPercentage);
    }
}
