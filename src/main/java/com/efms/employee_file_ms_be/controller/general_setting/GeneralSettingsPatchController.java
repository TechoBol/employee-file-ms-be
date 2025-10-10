package com.efms.employee_file_ms_be.controller.general_setting;

import com.efms.employee_file_ms_be.api.request.GeneralSettingsUpdateRequest;
import com.efms.employee_file_ms_be.api.response.GeneralSettingsResponse;
import com.efms.employee_file_ms_be.command.general_settings.GeneralSettingsPatchCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.GENERAL_SETTINGS_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.GENERAL_SETTINGS)
public class GeneralSettingsPatchController {

    private final GeneralSettingsPatchCmd command;

    @PatchMapping("/{id}")
    @Operation(summary = "Update General Settings")
    public ResponseEntity<GeneralSettingsResponse> patch(
            @PathVariable String id,
            @Valid @RequestBody GeneralSettingsUpdateRequest request) {
        command.setId(id);
        command.setGeneralSettingsUpdateRequest(request);
        command.execute();

        return ResponseEntity.ok(command.getGeneralSettingsResponse());
    }
}
