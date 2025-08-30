package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.PhotoUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    PhotoUploadResponseDto uploadPhoto(MultipartFile file, String entityType, Integer entityId);
    void deletePhoto(String publicId);
}