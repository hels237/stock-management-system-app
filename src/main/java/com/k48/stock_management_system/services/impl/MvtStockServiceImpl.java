package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.MvtStockDto;
import com.k48.stock_management_system.model.TypeMvtStock;
import com.k48.stock_management_system.repositories.MvtStockRepository;
import com.k48.stock_management_system.services.ArticleService;
import com.k48.stock_management_system.services.MvtStockService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MvtStockServiceImpl implements MvtStockService {
    private final MvtStockRepository mvtStockRepository;
    private final ArticleService articleService;
    private final ObjectValidator<MvtStockDto> objectValidator;

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        if (idArticle == null) {
            log.warn("ID article is NULL");
            return BigDecimal.valueOf(-1);
        }
        //verifi que l'article existe
        articleService.findById(idArticle);

        return mvtStockRepository.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStockDto> mvtStkArticle(Integer idArticle) {
        return mvtStockRepository.findAllByArticleId(idArticle).stream()
                .map(MvtStockDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public MvtStockDto entreeStock(MvtStockDto dto) {
        return entreePositive(dto, TypeMvtStock.ENTREE);
    }

    @Override
    public MvtStockDto sortieStock(MvtStockDto dto) {
        return sortieNegative(dto, TypeMvtStock.SORTIE);
    }

    @Override
    public MvtStockDto correctionStockPos(MvtStockDto dto) {
        return entreePositive(dto, TypeMvtStock.CORRECTION_POS);
    }

    @Override
    public MvtStockDto correctionStockNeg(MvtStockDto dto) {
        return sortieNegative(dto, TypeMvtStock.CORRECTION_NEG);
    }
    
    private MvtStockDto entreePositive(MvtStockDto dto, TypeMvtStock typeMvtStock) {
        objectValidator.validate(dto);

        dto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(dto.getQuantite().doubleValue())
                )
        );
        dto.setTypeMvt(typeMvtStock);
        return MvtStockDto.fromEntity(
                mvtStockRepository.save(MvtStockDto.toEntity(dto))
        );
    }

    private MvtStockDto sortieNegative(MvtStockDto dto, TypeMvtStock typeMvtStock) {
        objectValidator.validate(dto);

        dto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(dto.getQuantite().doubleValue()) * -1
                )
        );
        dto.setTypeMvt(typeMvtStock);
        return MvtStockDto.fromEntity(
                mvtStockRepository.save(MvtStockDto.toEntity(dto))
        );
    }
}