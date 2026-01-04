package com.efms.employee_file_ms_be.controller.file;

import com.efms.employee_file_ms_be.api.response.FileWithUrlResponse;
import com.efms.employee_file_ms_be.command.file.GetEmployeeFileCmd;
import com.efms.employee_file_ms_be.controller.Constants;
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
@RequestMapping(Constants.Path.FILE_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.FILE)
public class GetEmployeeFileController {

    private final GetEmployeeFileCmd cmd;

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<FileWithUrlResponse> getEmployeeFile(
            @PathVariable String employeeId) {
        cmd.setEmployeeId(employeeId);
        cmd.execute();

        return ResponseEntity.ok(cmd.getFileWithUrlResponse());
    }
}
