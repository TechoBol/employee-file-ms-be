package com.efms.employee_file_ms_be.model.mapper;

import com.efms.employee_file_ms_be.api.request.GeneralSettingsCreateRequest;
import com.efms.employee_file_ms_be.api.response.GeneralSettingsResponse;
import com.efms.employee_file_ms_be.model.domain.GeneralSettings;
import org.springframework.stereotype.Component;

/**
 * @author Josue Veliz
 */
@Component
public class GeneralSettingsMapper implements CustomMapper<GeneralSettingsResponse, GeneralSettingsCreateRequest, GeneralSettings> {

    @Override
    public GeneralSettings toEntity(GeneralSettingsCreateRequest generalSettingsCreateRequest) {
        GeneralSettings instance = new GeneralSettings();
        instance.setWorkingDaysPerMonth(generalSettingsCreateRequest.getWorkingDaysPerMonth());
        instance.setSeniorityIncreasePercentage(generalSettingsCreateRequest.getSeniorityIncreasePercentage());
        instance.setContributionAfpPercentage(generalSettingsCreateRequest.getContributionAfpPercentage());

        return instance;
    }

    @Override
    public GeneralSettingsResponse toDTO(GeneralSettings generalSettings) {
        return GeneralSettingsResponse.builder()
                .id(generalSettings.getId().toString())
                .companyId(generalSettings.getCompanyId().toString())
                .workingDaysPerMonth(generalSettings.getWorkingDaysPerMonth())
                .seniorityIncreasePercentage(generalSettings.getSeniorityIncreasePercentage())
                .contributionAfpPercentage(generalSettings.getContributionAfpPercentage())
                .createdAt(generalSettings.getCreatedAt())
                .updatedAt(generalSettings.getUpdatedAt())
                .build();
    }
}
