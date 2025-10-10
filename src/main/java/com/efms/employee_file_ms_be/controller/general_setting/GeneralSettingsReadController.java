package com.efms.employee_file_ms_be.controller.general_setting;

import com.efms.employee_file_ms_be.api.response.GeneralSettingsResponse;
import com.efms.employee_file_ms_be.command.general_settings.GeneralSettingsReadByCompanyIdCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.GENERAL_SETTINGS_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.GENERAL_SETTINGS)
public class GeneralSettingsReadController {

    private final GeneralSettingsReadByCompanyIdCmd command;

    @GetMapping
    @Operation(summary = "Get General Settings by company ID")
    public ResponseEntity<GeneralSettingsResponse> getByCompany() {
        command.execute();

        return ResponseEntity.ok(command.getGeneralSettingsResponse());
    }
}
