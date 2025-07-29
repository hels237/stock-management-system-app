package com.k48.stock_management_system.services.strategy;

import com.flickr4java.flickr.FlickrException;
import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.services.ArticleService;
import com.k48.stock_management_system.services.FlickrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service()
@Slf4j
public class SaveArticlePhoto   {
/*    private  FlickrService flickrService;
    private  ArticleService articleService;

    @Autowired
    public SaveArticlePhoto(FlickrService flickrService, ArticleService articleService) {
        this.flickrService = flickrService;
        this.articleService = articleService;
    }

    @Override
    public ArticleDto savePhoto(Integer id, InputStream photo, String titre) throws FlickrException {
        ArticleDto article = articleService.findById(id);
        String urlPhoto = flickrService.savePhoto(photo, titre);
        if (!StringUtils.hasLength(urlPhoto)) {
            throw new InvalidOperationException("Erreur lors de l'enregistrement de photo de l'article", ErrorCode.UPDATE_PHOTO_EXCEPTION);
        }
        article.setPhoto(urlPhoto);
        return articleService.save(article);
    }*/
}
