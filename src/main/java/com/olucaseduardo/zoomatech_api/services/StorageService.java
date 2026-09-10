package com.olucaseduardo.zoomatech_api.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final AmazonS3 s3;
    @Value("${AWS_BUCKET_NAME}")
    private String bucketName;

    public Optional<String> uploadFile(MultipartFile file) {
        String newPath = UUID.randomUUID().toString();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            String contentType = file.getContentType();
            if (contentType == null || contentType.equals("application/octet-stream")) {
                contentType = URLConnection.guessContentTypeFromName(file.getOriginalFilename());
            }
            metadata.setContentType(contentType);

            s3.putObject(new PutObjectRequest(this.bucketName, newPath, file.getInputStream(), metadata));

            return Optional.of(newPath);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<String> replaceFile(MultipartFile newFile, String oldFilePath) {
        deleteFile(oldFilePath);
        return this.uploadFile(newFile);
    }

    public Optional<String> uploadDocument(MultipartFile file, String folder) {
        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document.pdf";
        String extension = "";
        int dotIdx = originalName.lastIndexOf('.');
        if (dotIdx >= 0) {
            extension = originalName.substring(dotIdx);
        }

        String hashName = UUID.randomUUID().toString().replace("-", "") + extension;
        String newPath = (folder != null && !folder.isBlank()) ? folder + "/" + hashName : hashName;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            String contentType = file.getContentType();
            if (contentType == null || contentType.equals("application/octet-stream")) {
                contentType = URLConnection.guessContentTypeFromName(originalName);
            }
            if (contentType == null) {
                contentType = "application/pdf";
            }
            metadata.setContentType(contentType);
            metadata.setContentDisposition("inline; filename=\"" + originalName + "\"");

            s3.putObject(new PutObjectRequest(this.bucketName, newPath, file.getInputStream(), metadata));

            return Optional.of(newPath);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<String> replaceDocument(MultipartFile newFile, String oldFilePath, String folder) {
        deleteFile(oldFilePath);
        return this.uploadDocument(newFile, folder);
    }

    public void deleteFile(String filePath) {
        if (filePath != null && !filePath.isBlank()) {
            try {
                s3.deleteObject(this.bucketName, filePath);
            } catch (Exception ignored) {
            }
        }
    }
}
