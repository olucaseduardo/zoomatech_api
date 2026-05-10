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

    public Optional<String> uploadFile(MultipartFile file, String filePath) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            String contentType = file.getContentType();
            if (contentType == null || contentType.equals("application/octet-stream")) {
                contentType = URLConnection.guessContentTypeFromName(file.getOriginalFilename());
            }
            metadata.setContentType(contentType);

            s3.putObject(new PutObjectRequest(this.bucketName, filePath, file.getInputStream(), metadata));

            return Optional.of(filePath);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<String> uploadFile(MultipartFile file) {
        String defaultPath = UUID.randomUUID().toString();
        return this.uploadFile(file, defaultPath);
    }
}
