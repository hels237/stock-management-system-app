package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.controller.api.PhotoApi;
import com.k48.stock_management_system.dto.PhotoUploadResponseDto;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.services.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/gestiondestock/v1/photos")
@RequiredArgsConstructor
public class PhotoController implements PhotoApi {

    private final PhotoService photoService;
    
    private static final List<String> ALLOWED_ENTITY_TYPES = Arrays.asList(
            "article", "utilisateur", "client", "fournisseur", "entreprise");
    @Override
    public PhotoUploadResponseDto uploadPhoto(MultipartFile file, String entityType, Integer entityId) {
        validateEntityType(entityType);
        validateFile(file);
        
        return photoService.uploadPhoto(file, entityType.toLowerCase(), entityId);
    }

    @Override
    public void deletePhoto(String publicId) {
        photoService.deletePhoto(publicId);
    }

    @Override
    public org.springframework.http.ResponseEntity<byte[]> getArticlePhoto(Integer id) {
        return photoService.getEntityPhoto("article", id);
    }

    @Override
    public org.springframework.http.ResponseEntity<byte[]> getUtilisateurPhoto(Integer id) {
        return photoService.getEntityPhoto("utilisateur", id);
    }

    @Override
    public org.springframework.http.ResponseEntity<byte[]> getClientPhoto(Integer id) {
        return photoService.getEntityPhoto("client", id);
    }

    @Override
    public org.springframework.http.ResponseEntity<byte[]> getFournisseurPhoto(Integer id) {
        return photoService.getEntityPhoto("fournisseur", id);
    }

    private void validateEntityType(String entityType) {
        if (!ALLOWED_ENTITY_TYPES.contains(entityType.toLowerCase())) {
            throw new InvalidOperationException(
                "Type d'entité non supporté: " + entityType, 
                ErrorCode.INVALID_OPERATION_EXCEPTION
            );
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidOperationException("Le fichier est vide", ErrorCode.INVALID_OPERATION_EXCEPTION);
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidOperationException("Le fichier doit être une image", ErrorCode.INVALID_OPERATION_EXCEPTION);
        }
    }
}