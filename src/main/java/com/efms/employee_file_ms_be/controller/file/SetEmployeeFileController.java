package com.efms.employee_file_ms_be.controller.file;

import com.efms.employee_file_ms_be.api.response.FileResponse;
import com.efms.employee_file_ms_be.command.file.SetEmployeeFileCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.FILE_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.FILE)
public class SetEmployeeFileController {

    private final SetEmployeeFileCmd cmd;

    @PostMapping(value = "/employees/{employeeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> setEmployeeFileSections(
            @PathVariable String employeeId,
            @RequestParam("sections") List<String> sectionNames,
            @RequestPart("files") List<MultipartFile> files) {

        cmd.setEmployeeId(employeeId);
        cmd.setFiles(files);
        cmd.setSectionNames(sectionNames);

        cmd.execute();

        return ResponseEntity.ok(cmd.getFileResponse());
    }
}