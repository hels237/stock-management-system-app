package com.k48.stock_management_system.services.strategy;

import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.dto.PhotoUploadResponseDto;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.services.ArticleService;
import com.k48.stock_management_system.services.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaveArticlePhoto {
    
    private final PhotoService photoService;
    private final ArticleService articleService;

    public ArticleDto savePhoto(Integer id, MultipartFile photo) {
        ArticleDto article = articleService.findById(id);
        PhotoUploadResponseDto uploadResult = photoService.uploadPhoto(photo, "article", id);
        
        if (!StringUtils.hasLength(uploadResult.getUrl())) {
            throw new InvalidOperationException("Erreur lors de l'enregistrement de photo de l'article", ErrorCode.UPDATE_PHOTO_EXCEPTION);
        }
        
        article.setPhoto(uploadResult.getUrl());
        return articleService.save(article);
    }
}
