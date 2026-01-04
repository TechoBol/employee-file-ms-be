package com.efms.employee_file_ms_be.handler;

import com.efms.employee_file_ms_be.exception.FileDeleteException;
import com.efms.employee_file_ms_be.exception.FileEmptyException;
import com.efms.employee_file_ms_be.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Josue Veliz
 */
@Component
@RequiredArgsConstructor
public class FileUploadHandler {

    private final MinioService minioService;

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileEmptyException(file.getName());
        }
        return minioService.upload(file);
    }

    public List<String> uploadFiles(List<MultipartFile> files) {
        return files.stream()
                .filter(file -> !file.isEmpty())
                .map(this::uploadFile)
                .toList();
    }

    public void deleteFile(String uuidFileName) {
        try {
            minioService.deleteObject(uuidFileName);
        } catch (Exception e) {
            throw new FileDeleteException(uuidFileName, e.getMessage());
        }
    }

    public void deleteOldFiles(List<String> uuidFileNames) {
        uuidFileNames.forEach(this::deleteFile);
    }

    public String getPresignedUrl(String uuidFileName) {
        return minioService.presignedUrl(uuidFileName);
    }
}
