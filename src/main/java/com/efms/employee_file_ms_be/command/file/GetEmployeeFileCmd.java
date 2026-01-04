package com.efms.employee_file_ms_be.command.file;

/**
 * @author Josue Veliz
 */
import com.efms.employee_file_ms_be.api.response.FileWithUrlResponse;
import com.efms.employee_file_ms_be.api.response.UnitFileWithUrlResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.FileNotFoundException;
import com.efms.employee_file_ms_be.handler.FileUploadHandler;
import com.efms.employee_file_ms_be.model.domain.File;
import com.efms.employee_file_ms_be.model.domain.UnitFile;
import com.efms.employee_file_ms_be.model.repository.FileRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class GetEmployeeFileCmd implements Command {

    @Setter
    private String employeeId;

    @Getter
    private FileWithUrlResponse fileWithUrlResponse;

    private final FileRepository fileRepository;
    private final FileUploadHandler fileUploadHandler;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        UUID empId = UUID.fromString(employeeId);


        File file = fileRepository.findByEmployeeIdAndCompanyId(empId, companyId)
                .orElseThrow(() -> new FileNotFoundException(employeeId));

        fileWithUrlResponse = buildFileWithUrlResponse(file);
    }

    private FileWithUrlResponse buildFileWithUrlResponse(File file) {
        FileWithUrlResponse response = new FileWithUrlResponse();
        response.setId(file.getId());
        response.setEmployeeId(file.getEmployee().getId());
        response.setCompanyId(file.getCompanyId());
        response.setSections(file.getSections().stream()
                .map(this::buildUnitFileWithUrlResponse)
                .toList());
        response.setCreatedAt(file.getCreatedAt());
        response.setUpdatedAt(file.getUpdatedAt());
        return response;
    }

    private UnitFileWithUrlResponse buildUnitFileWithUrlResponse(UnitFile unitFile) {
        UnitFileWithUrlResponse response = new UnitFileWithUrlResponse();
        response.setId(unitFile.getId());
        response.setOriginalName(unitFile.getOriginalName());
        response.setSection(unitFile.getSection());
        response.setDescription(unitFile.getDescription());
        response.setUploadBy(unitFile.getUploadBy());
        response.setUrl(fileUploadHandler.getPresignedUrl(unitFile.getUuidFileName()));
        response.setCreatedAt(unitFile.getCreatedAt());
        response.setUpdatedAt(unitFile.getUpdatedAt());
        return response;
    }
}
