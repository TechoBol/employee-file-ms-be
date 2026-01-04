package com.efms.employee_file_ms_be.controller.general_setting;

import com.efms.employee_file_ms_be.api.request.GeneralSettingsCreateRequest;
import com.efms.employee_file_ms_be.api.response.GeneralSettingsResponse;
import com.efms.employee_file_ms_be.command.general_settings.GeneralSettingsCreateCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.GENERAL_SETTINGS_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.GENERAL_SETTINGS)
public class GeneralSettingsCreateController {

    private final GeneralSettingsCreateCmd command;

    @PostMapping
    @Operation(summary = "Create new General Settings")
    public ResponseEntity<GeneralSettingsResponse> create(@Valid @RequestBody GeneralSettingsCreateRequest request) {
        command.setGeneralSettingsCreateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getGeneralSettingsResponse());
    }
}
