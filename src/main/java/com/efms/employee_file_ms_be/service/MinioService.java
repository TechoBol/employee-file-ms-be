package com.efms.employee_file_ms_be.service;

import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.expiry-seconds}")
    private Integer expirySeconds;

    private boolean isAvailable = false;

    @PostConstruct
    public void init() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucket).build()
                );
            }
            isAvailable = true;
            log.info("MinIO successfully initialized");
        } catch (Exception e) {
            isAvailable = false;
            log.warn("MinIO is not available. Application will continue without file storage: {}", e.getMessage());
        }
    }

    public String upload(MultipartFile file) {
        if (!isAvailable) {
            throw new RuntimeException("Storage service is not available");
        }

        try {
            String extension = getExtension(file.getOriginalFilename());
            String objectName = UUID.randomUUID() + extension;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(file.getInputStream(), -1, 10485760)
                            .contentType(file.getContentType())
                            .build()
            );

            return objectName;
        } catch (Exception e) {
            log.error("Error uploading file to MinIO", e);
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    public List<String> uploadMany(List<MultipartFile> files) {
        return files.stream()
                .map(this::upload)
                .toList();
    }

    public byte[] download(String objectName) {
        if (!isAvailable) {
            throw new RuntimeException("Storage service is not available");
        }

        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        )) {
            return stream.readAllBytes();
        } catch (Exception e) {
            log.error("Error downloading file from MinIO", e);
            throw new RuntimeException("Error downloading file: " + e.getMessage());
        }
    }

    public String presignedUrl(String objectName) {
        if (!isAvailable) {
            throw new RuntimeException("Storage service is not available");
        }

        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .expiry(expirySeconds)
                            .method(Method.GET)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error generating presigned URL", e);
            throw new RuntimeException("Error generating presigned URL: " + e.getMessage());
        }
    }

    public void deleteObject(String objectName) {
        if (!isAvailable) {
            throw new RuntimeException("Storage service is not available");
        }

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error deleting file from MinIO", e);
            throw new RuntimeException("Error deleting file: " + e.getMessage());
        }
    }

    public boolean isServiceAvailable() {
        return isAvailable;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}
