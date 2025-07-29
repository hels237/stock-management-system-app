package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.MvtStockDto;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStockService {
    BigDecimal stockReelArticle(Integer idArticle);

    List<MvtStockDto> mvtStkArticle(Integer idArticle);

    MvtStockDto entreeStock(MvtStockDto dto);

    MvtStockDto sortieStock(MvtStockDto dto);

    MvtStockDto correctionStockPos(MvtStockDto dto);

    MvtStockDto correctionStockNeg(MvtStockDto dto);
}
