package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.services.StockAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gestiondestock/v1/stockalerts")
@Tag(name = "Stock Alerts", description = "API pour la gestion des alertes de stock")
@RequiredArgsConstructor
public class StockAlertController {

    private final StockAlertService stockAlertService;

    @PostMapping("/check/{articleId}")
    @Operation(summary = "Vérifier l'alerte de stock pour un article")
    public ResponseEntity<Void> checkStockAlert(@PathVariable Integer articleId) {
        stockAlertService.checkStockAlert(articleId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-all")
    @Operation(summary = "Vérifier les alertes de stock pour tous les articles")
    public ResponseEntity<Void> checkAllStockAlerts() {
        stockAlertService.checkAllStockAlerts();
        return ResponseEntity.ok().build();
    }
}