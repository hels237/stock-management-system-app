package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.PhotoUploadResponseDto;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.services.PhotoService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Slf4j
public class PhotoServiceImpl implements PhotoService {

    private final MinioClient minioClient;
    private final String bucketName;
    private final String minioUrl;

    public PhotoServiceImpl(@Value("${minio.endpoint:http://localhost:9000}") String endpoint,
                           @Value("${minio.access-key:minioadmin}") String accessKey,
                           @Value("${minio.secret-key:minioadmin}") String secretKey,
                           @Value("${minio.bucket-name:stock-photos}") String bucketName) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucketName;
        this.minioUrl = endpoint;
    }

    @Override
    public PhotoUploadResponseDto uploadPhoto(MultipartFile file, String entityType, Integer entityId) {
        try {
            ensureBucketExists();
            
            String fileName = entityType + "/" + entityId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            
            InputStream inputStream = file.getInputStream();
            
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );

            String url = minioUrl + "/" + bucketName + "/" + fileName;

            return PhotoUploadResponseDto.builder()
                    .url(url)
                    .publicId(fileName)
                    .message("Photo téléchargée avec succès")
                    .build();

        } catch (Exception e) {
            log.error("Erreur lors du téléchargement de la photo", e);
            throw new InvalidOperationException("Erreur lors du téléchargement de la photo", ErrorCode.UPDATE_PHOTO_EXCEPTION);
        }
    }

    private void ensureBucketExists() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                io.minio.BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!bucketExists) {
                minioClient.makeBucket(
                    io.minio.MakeBucketArgs.builder().bucket(bucketName).build()
                );
                log.info("Bucket '{}' créé avec succès", bucketName);
            }
        } catch (Exception e) {
            log.error("Erreur lors de la création du bucket", e);
        }
    }

    @Override
    public void deletePhoto(String fileName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build()
            );
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de la photo", e);
            throw new InvalidOperationException("Erreur lors de la suppression de la photo", ErrorCode.UPDATE_PHOTO_EXCEPTION);
        }
    }
}