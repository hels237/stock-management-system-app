package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.model.Article;
import com.k48.stock_management_system.repositories.ArticleRepository;
import com.k48.stock_management_system.services.MvtStockService;
import com.k48.stock_management_system.services.StockAlertService;
import com.k48.stock_management_system.notificationConfig.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockAlertServiceImpl implements StockAlertService {

    private final ArticleRepository articleRepository;
    private final MvtStockService mvtStockService;
    private final NotificationService notificationService;

    @Override
    public void checkStockAlert(Integer articleId) {
        Article article = articleRepository.findById(articleId).orElse(null);
        if (article == null || article.getSeuilMinimum() == null) {
            return;
        }

        Integer currentStock = mvtStockService.stockReelArticle(articleId).intValue();
        
        if (currentStock <= article.getSeuilMinimum()) {
            log.warn("Stock faible détecté pour l'article {} : stock actuel = {}, seuil minimum = {}", 
                    article.getDesignation(), currentStock, article.getSeuilMinimum());
            
            notificationService.sendStockAlert(article, currentStock, article.getSeuilMinimum());
        }
    }

    @Override
    public void checkAllStockAlerts() {
        List<Article> articles = articleRepository.findAll();
        
        for (Article article : articles) {
            if (article.getSeuilMinimum() != null) {
                checkStockAlert(article.getId());
            }
        }
    }
}