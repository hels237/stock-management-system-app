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

            // ENCODE: Remplace les slashes par des underscores pour l'URL
            String encodedPublicId = fileName.replace("/", "_SLASH_");
            
            return PhotoUploadResponseDto.builder()
                    .url(url)
                    .publicId(encodedPublicId)  // PublicId encodé pour l'URL
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

    @Override
    public org.springframework.http.ResponseEntity<byte[]> getPhoto(String publicId) {
        try {
            InputStream inputStream = minioClient.getObject(
                io.minio.GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(publicId)
                    .build()
            );

            byte[] imageBytes = inputStream.readAllBytes();
            inputStream.close();

            String contentType = getContentType(publicId);

            return org.springframework.http.ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .header("Cache-Control", "max-age=31536000") // Cache 1 an
                    .header("ETag", String.valueOf(publicId.hashCode()))
                    .body(imageBytes);

        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la photo: {}", publicId, e);
            return org.springframework.http.ResponseEntity.notFound().build();
        }
    }

    @Override
    public org.springframework.http.ResponseEntity<byte[]> getEntityPhoto(String entityType, Integer entityId) {
        try {
            // CONSTRUCTION: Génère le pattern de nom de fichier basé sur entityType et entityId
            String objectPrefix = entityType + "/" + entityId + "_";
            
            // RECHERCHE: Trouve le fichier dans MinIO avec ce préfixe
            Iterable<io.minio.Result<io.minio.messages.Item>> results = minioClient.listObjects(
                io.minio.ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(objectPrefix)
                    .build()
            );
            
            // RÉCUPÉRATION: Prend le premier fichier trouvé
            for (io.minio.Result<io.minio.messages.Item> result : results) {
                io.minio.messages.Item item = result.get();
                String objectName = item.objectName();
                
                // RÉCUPÉRATION: Télécharge l'image depuis MinIO
                InputStream inputStream = minioClient.getObject(
                    io.minio.GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
                );

                byte[] imageBytes = inputStream.readAllBytes();
                inputStream.close();

                String contentType = getContentType(objectName);
                String entityKey = entityType + "_" + entityId;

                // HEADERS OPTIMISÉS: Pour une consommation frontend parfaite
                return org.springframework.http.ResponseEntity.ok()
                        .header("Content-Type", contentType)
                        .header("Content-Length", String.valueOf(imageBytes.length))
                        .header("Cache-Control", "public, max-age=31536000, immutable") // Cache 1 an
                        .header("ETag", String.valueOf(entityKey.hashCode()))
                        .header("Accept-Ranges", "bytes")
                        .header("Access-Control-Allow-Origin", "*") // CORS pour frontend
                        .header("Access-Control-Expose-Headers", "Content-Length, Content-Type")
                        .body(imageBytes);
            }
            
            // PLACEHOLDER: Retourne une image par défaut si aucune photo trouvée
            return getDefaultImage(entityType);
            
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la photo pour {} ID {}", entityType, entityId, e);
            return getDefaultImage(entityType);
        }
    }
    
    // IMAGE PAR DÉFAUT: Retourne une image placeholder pour éviter les erreurs frontend
    private org.springframework.http.ResponseEntity<byte[]> getDefaultImage(String entityType) {
        // IMAGE 1x1 TRANSPARENTE: Base64 d'un pixel transparent PNG
        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChAI9jU77zgAAAABJRU5ErkJggg==";
        byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);
        
        return org.springframework.http.ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .header("Content-Length", String.valueOf(imageBytes.length))
                .header("Cache-Control", "public, max-age=3600") // Cache 1h pour placeholder
                .header("Access-Control-Allow-Origin", "*")
                .body(imageBytes);
    }

    private String getContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            default -> "image/jpeg";
        };
    }
}