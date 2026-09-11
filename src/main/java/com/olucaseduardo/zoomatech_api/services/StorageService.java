package com.olucaseduardo.zoomatech_api.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final AmazonS3 s3;
    @Value("${AWS_BUCKET_NAME}")
    private String bucketName;

    public Optional<String> uploadFile(MultipartFile file) {
        String newPath = UUID.randomUUID().toString();
        File tempFile = null;
        try {
            tempFile = Files.createTempFile("upload-", ".tmp").toFile();
            file.transferTo(tempFile);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(tempFile.length());
            String contentType = file.getContentType();
            if (contentType == null || contentType.equals("application/octet-stream")) {
                contentType = URLConnection.guessContentTypeFromName(file.getOriginalFilename());
            }
            metadata.setContentType(contentType);

            PutObjectRequest putRequest = new PutObjectRequest(this.bucketName, newPath, tempFile);
            putRequest.setMetadata(metadata);
            s3.putObject(putRequest);

            return Optional.of(newPath);
        } catch (AmazonClientException | IOException e) {
            log.error("Falha ao realizar upload para o S3: {}", e.getMessage(), e);
            return Optional.empty();
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
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
        File tempFile = null;

        try {
            tempFile = Files.createTempFile("doc-", extension).toFile();
            file.transferTo(tempFile);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(tempFile.length());
            String contentType = file.getContentType();
            if (contentType == null || contentType.equals("application/octet-stream")) {
                contentType = URLConnection.guessContentTypeFromName(originalName);
            }
            if (contentType == null) {
                contentType = "application/pdf";
            }
            metadata.setContentType(contentType);
            metadata.setContentDisposition("inline; filename=\"" + originalName + "\"");

            PutObjectRequest putRequest = new PutObjectRequest(this.bucketName, newPath, tempFile);
            putRequest.setMetadata(metadata);
            s3.putObject(putRequest);

            return Optional.of(newPath);
        } catch (AmazonClientException | IOException e) {
            log.error("Falha ao realizar upload de documento para o S3: {}", e.getMessage(), e);
            return Optional.empty();
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
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
            } catch (Exception e) {
                log.warn("Não foi possível excluir o arquivo {} do S3: {}", filePath, e.getMessage());
            }
        }
    }

    public String generatePresignedUrl(String filePath, int expirationMinutes) {
        if (filePath == null || filePath.isBlank()) {
            return null;
        }
        if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
            return filePath;
        }
        try {
            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000L * 60 * expirationMinutes;
            expiration.setTime(expTimeMillis);

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(this.bucketName, filePath)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);

            return s3.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (Exception e) {
            log.error("Erro ao gerar URL pré-assinada para {}: {}", filePath, e.getMessage());
            return filePath;
        }
    }

    public String generatePresignedUrl(String filePath) {
        return generatePresignedUrl(filePath, 120);
    }
}
