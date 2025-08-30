package com.k48.stock_management_system.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MinioConfig implements CommandLineRunner {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioConfig(@Value("${minio.endpoint:http://localhost:9000}") String endpoint,
                      @Value("${minio.access-key:minioadmin}") String accessKey,
                      @Value("${minio.secret-key:minioadmin}") String secretKey,
                      @Value("${minio.bucket-name:stock-photos}") String bucketName) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucketName;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket '{}' créé avec succès", bucketName);
            } else {
                log.info("Bucket '{}' existe déjà", bucketName);
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'initialisation du bucket MinIO", e);
        }
    }
}