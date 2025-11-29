package com.efms.employee_file_ms_be.command.file;

import com.efms.employee_file_ms_be.api.response.FileResponse;
import com.efms.employee_file_ms_be.api.response.UnitFileResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.FileSectionMismatchException;
import com.efms.employee_file_ms_be.exception.NoFilesProvidedException;
import com.efms.employee_file_ms_be.handler.FileUploadHandler;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.domain.File;
import com.efms.employee_file_ms_be.model.domain.UnitFile;
import com.efms.employee_file_ms_be.model.repository.FileRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class SetEmployeeFileCmd implements Command {

    @Setter
    private String employeeId;

    @Setter
    private List<MultipartFile> files;

    @Setter
    private List<String> sectionNames;

    @Getter
    private FileResponse fileResponse;

    private final FileRepository fileRepository;
    private final FileUploadHandler fileUploadHandler;

    @Override
    public void execute() {
        validateInput();
        System.out.println(sectionNames.toString());
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        File file = fileRepository.findByEmployeeIdAndCompanyId(
                UUID.fromString(employeeId),
                companyId
        ).orElseGet(() -> createNewFile(companyId));

        deleteOldFiles(file);

        file.getSections().clear();
        file.getSections().addAll(processFiles());

        file.setUpdatedAt(LocalDateTime.now());
        if (file.getCreatedAt() == null) {
            file.setCreatedAt(LocalDateTime.now());
        }

        file = fileRepository.save(file);
        fileResponse = buildFileResponse(file);
    }

    private File createNewFile(UUID companyId) {
        File file = new File();
        Employee employee = new Employee();
        employee.setId(UUID.fromString(employeeId));
        file.setEmployee(employee);
        file.setCompanyId(companyId);
        file.setSections(new ArrayList<>());
        file.setUpdatedAt(LocalDateTime.now());
        file.setCreatedAt(LocalDateTime.now());
        return file;
    }

    private void validateInput() {
        if (files == null || files.isEmpty()) {
            throw new NoFilesProvidedException();
        }

        if (files.size() != sectionNames.size()) {
            throw new FileSectionMismatchException(
                    files.size(),
                    sectionNames.size()
            );
        }
    }

    private List<UnitFile> processFiles() {
        List<UnitFile> newSections = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            String uuidFileName = fileUploadHandler.uploadFile(files.get(i));

            UnitFile unitFile = new UnitFile();
            unitFile.setId(UUID.randomUUID());
            unitFile.setUuidFileName(uuidFileName);
            unitFile.setOriginalName(files.get(i).getOriginalFilename());
            unitFile.setSection(sectionNames.get(i));
            unitFile.setCreatedAt(LocalDateTime.now());
            unitFile.setUpdatedAt(LocalDateTime.now());

            newSections.add(unitFile);
        }

        return newSections;
    }

    private void deleteOldFiles(File file) {
        if (file.getSections() != null && !file.getSections().isEmpty()) {
            List<String> oldFileNames = file.getSections().stream()
                    .map(UnitFile::getUuidFileName)
                    .toList();
            fileUploadHandler.deleteOldFiles(oldFileNames);
        }
    }

    private FileResponse buildFileResponse(File file) {
        FileResponse fileResponse = new FileResponse();
        fileResponse.setId(file.getId());
        fileResponse.setEmployeeId(UUID.fromString(employeeId));
        fileResponse.setCompanyId(file.getCompanyId());
        List<UnitFileResponse> unitFileResponses = new ArrayList<>();
        for (UnitFile unitFile :  file.getSections()) {
            unitFileResponses.add(buildFileUnitResponse(unitFile));
        }
        fileResponse.setSections(unitFileResponses);
        fileResponse.setCreatedAt(file.getCreatedAt());
        fileResponse.setUpdatedAt(file.getUpdatedAt());
        return fileResponse;
    }

    private UnitFileResponse buildFileUnitResponse(UnitFile unitFile) {
        UnitFileResponse unitFileResponse = new UnitFileResponse();
        unitFileResponse.setId(unitFile.getId());
        unitFileResponse.setOriginalName(unitFile.getOriginalName());
        unitFileResponse.setSection(unitFile.getSection());
        unitFileResponse.setUuidFileName(unitFile.getUuidFileName());
        unitFileResponse.setCreatedAt(unitFile.getCreatedAt());
        unitFileResponse.setUpdatedAt(unitFile.getUpdatedAt());
        return unitFileResponse;
    }
}
