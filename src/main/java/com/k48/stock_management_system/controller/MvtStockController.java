package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.controller.api.MvtStockApi;
import com.k48.stock_management_system.dto.MvtStockDto;
import com.k48.stock_management_system.services.MvtStockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@Tag(name = "Mouvement de Stock", description = "API pour la gestion des mouvements de stock")
@RestController
@RequestMapping(APP_ROOT + "/mvtstock")
@RequiredArgsConstructor
public class MvtStockController implements MvtStockApi {

    private final MvtStockService mvtStockService;

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        return mvtStockService.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStockDto> mvtStkArticle(Integer idArticle) {
        return mvtStockService.mvtStkArticle(idArticle);
    }

    @Override
    public MvtStockDto entreeStock(MvtStockDto dto) {
        return mvtStockService.entreeStock(dto);
    }

    @Override
    public MvtStockDto sortieStock(MvtStockDto dto) {
        return mvtStockService.sortieStock(dto);
    }

    @Override
    public MvtStockDto correctionStockPos(MvtStockDto dto) {
        return mvtStockService.correctionStockPos(dto);
    }

    @Override
    public MvtStockDto correctionStockNeg(MvtStockDto dto) {
        return mvtStockService.correctionStockNeg(dto);
    }

}
