package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.ArticleDto;

public interface StockAlertService {
    
    void checkStockAlert(Integer articleId);
    
    void checkAllStockAlerts();
    
}