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
        try {
            s3.deleteObject(this.bucketName, oldFilePath);
        } catch (Exception ignored) {
        }
        return this.uploadFile(newFile);
    }
}
