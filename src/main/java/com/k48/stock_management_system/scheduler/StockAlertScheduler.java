package com.k48.stock_management_system.scheduler;

import com.k48.stock_management_system.services.StockAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockAlertScheduler {

    private final StockAlertService stockAlertService;

    @Scheduled(fixedRate = 300000) // Toutes les 5 minutes
    public void checkStockAlerts() {
        log.info("Vérification automatique des alertes de stock...");
        stockAlertService.checkAllStockAlerts();
        log.info("Vérification des alertes de stock terminée");
    }
}